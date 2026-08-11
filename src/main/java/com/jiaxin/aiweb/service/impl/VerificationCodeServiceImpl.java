package com.jiaxin.aiweb.service.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.jiaxin.aiweb.config.AuthCodeProperties;
import com.jiaxin.aiweb.exception.BusinessException;
import com.jiaxin.aiweb.exception.ErrorCode;
import com.jiaxin.aiweb.model.dto.user.VerificationCodeLoginRequest;
import com.jiaxin.aiweb.model.dto.user.VerificationCodeSendRequest;
import com.jiaxin.aiweb.model.enums.VerificationCodeTypeEnum;
import com.jiaxin.aiweb.model.vo.LoginUserVO;
import com.jiaxin.aiweb.service.UserService;
import com.jiaxin.aiweb.service.VerificationCodeSender;
import com.jiaxin.aiweb.service.VerificationCodeService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 统一验证码服务实现。
 */
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static final String CODE_KEY_PREFIX = "auth:login_code:";

    private static final String RESEND_KEY_PREFIX = "auth:login_code_resend:";

    private static final String TARGET_DAY_KEY_PREFIX = "auth:login_code_target_day:";

    private static final String IP_HOUR_KEY_PREFIX = "auth:login_code_ip_hour:";

    private static final String FAIL_KEY_PREFIX = "auth:login_code_fail:";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d{6,20}$");

    private final SecureRandom secureRandom = new SecureRandom();

    @Resource
    private AuthCodeProperties authCodeProperties;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private UserService userService;

    @Resource
    private List<VerificationCodeSender> verificationCodeSenders;

    @Override
    public void sendCode(VerificationCodeSendRequest verificationCodeSendRequest, HttpServletRequest request) {
        if (verificationCodeSendRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VerificationCodeTypeEnum typeEnum = getTypeEnum(verificationCodeSendRequest.getType());
        String target = normalizeTarget(typeEnum, verificationCodeSendRequest.getTarget());
        String ip = getClientIp(request);
        checkFailLock(typeEnum, target);
        String resendKey = buildKey(RESEND_KEY_PREFIX, typeEnum, target);
        RBucket<String> resendBucket = redissonClient.getBucket(resendKey);
        if (Boolean.TRUE.equals(resendBucket.isExists())) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "验证码发送过于频繁，请稍后再试");
        }
        increaseCounter(buildKey(TARGET_DAY_KEY_PREFIX, typeEnum, target),
                authCodeProperties.getDailyTargetLimit(), 1, TimeUnit.DAYS,
                "今日验证码发送次数已达上限");
        increaseCounter(IP_HOUR_KEY_PREFIX + ip,
                authCodeProperties.getHourlyIpLimit(), 1, TimeUnit.HOURS,
                "当前 IP 请求验证码过于频繁");

        String code = generateCode();
        String salt = UUID.randomUUID().toString().replace("-", "");
        String storedCode = salt + ":" + hashCode(salt, code);
        RBucket<String> codeBucket = redissonClient.getBucket(buildKey(CODE_KEY_PREFIX, typeEnum, target));
        codeBucket.set(storedCode, authCodeProperties.getExpireMinutes(), TimeUnit.MINUTES);
        resendBucket.set("1", authCodeProperties.getResendIntervalSeconds(), TimeUnit.SECONDS);
        try {
            getSender(typeEnum).send(target, code);
        } catch (RuntimeException e) {
            codeBucket.delete();
            resendBucket.delete();
            throw e;
        }
    }

    @Override
    public LoginUserVO loginByCode(VerificationCodeLoginRequest verificationCodeLoginRequest,
                                   HttpServletRequest request) {
        if (verificationCodeLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VerificationCodeTypeEnum typeEnum = getTypeEnum(verificationCodeLoginRequest.getType());
        String target = normalizeTarget(typeEnum, verificationCodeLoginRequest.getTarget());
        String code = verificationCodeLoginRequest.getCode();
        if (StrUtil.isBlank(code) || !code.matches("^\\d{6}$")) {
            recordFail(typeEnum, target);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或已过期");
        }
        checkFailLock(typeEnum, target);
        String codeKey = buildKey(CODE_KEY_PREFIX, typeEnum, target);
        RBucket<String> codeBucket = redissonClient.getBucket(codeKey);
        String storedCode = codeBucket.get();
        if (StrUtil.isBlank(storedCode) || !verifyCode(storedCode, code)) {
            recordFail(typeEnum, target);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或已过期");
        }
        codeBucket.delete();
        redissonClient.getAtomicLong(buildKey(FAIL_KEY_PREFIX, typeEnum, target)).delete();
        return userService.userLoginByCode(typeEnum.getValue(), target, request);
    }

    private VerificationCodeTypeEnum getTypeEnum(String type) {
        VerificationCodeTypeEnum typeEnum = VerificationCodeTypeEnum.getEnumByValue(type);
        if (typeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码类型错误");
        }
        return typeEnum;
    }

    private String normalizeTarget(VerificationCodeTypeEnum typeEnum, String target) {
        if (StrUtil.isBlank(target)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号或邮箱不能为空");
        }
        if (VerificationCodeTypeEnum.PHONE.equals(typeEnum)) {
            String phone = StrUtil.cleanBlank(target);
            if (phone.matches("^1[3-9]\\d{9}$")) {
                phone = "+86" + phone;
            }
            if (phone.startsWith("00")) {
                phone = "+" + phone.substring(2);
            }
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误，请使用 +86 开头的国际格式");
            }
            return phone;
        }
        String email = target.trim().toLowerCase(Locale.ROOT);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        return email;
    }

    private VerificationCodeSender getSender(VerificationCodeTypeEnum typeEnum) {
        return verificationCodeSenders.stream()
                .filter(sender -> typeEnum.equals(sender.getType()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.OPERATION_ERROR, "验证码发送通道未配置"));
    }

    private String generateCode() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private String hashCode(String salt, String code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest((salt + ":" + code).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "验证码加密失败");
        }
    }

    private boolean verifyCode(String storedCode, String inputCode) {
        String[] parts = storedCode.split(":", 2);
        if (parts.length != 2) {
            return false;
        }
        String inputHash = hashCode(parts[0], inputCode);
        return MessageDigest.isEqual(
                parts[1].getBytes(StandardCharsets.UTF_8),
                inputHash.getBytes(StandardCharsets.UTF_8)
        );
    }

    private void checkFailLock(VerificationCodeTypeEnum typeEnum, String target) {
        RAtomicLong failCounter = redissonClient.getAtomicLong(buildKey(FAIL_KEY_PREFIX, typeEnum, target));
        if (failCounter.get() >= authCodeProperties.getMaxFailCount()) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "验证码错误次数过多，请稍后再试");
        }
    }

    private void recordFail(VerificationCodeTypeEnum typeEnum, String target) {
        RAtomicLong failCounter = redissonClient.getAtomicLong(buildKey(FAIL_KEY_PREFIX, typeEnum, target));
        long failCount = failCounter.incrementAndGet();
        if (failCount == 1) {
            failCounter.expire(authCodeProperties.getFailLockMinutes(), TimeUnit.MINUTES);
        }
        if (failCount >= authCodeProperties.getMaxFailCount()) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "验证码错误次数过多，请稍后再试");
        }
    }

    private void increaseCounter(String key, int limit, long ttl, TimeUnit timeUnit, String message) {
        RAtomicLong counter = redissonClient.getAtomicLong(key);
        long count = counter.incrementAndGet();
        if (count == 1) {
            counter.expire(ttl, timeUnit);
        }
        if (count > limit) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, message);
        }
    }

    private String buildKey(String prefix, VerificationCodeTypeEnum typeEnum, String target) {
        return prefix + typeEnum.getValue() + ":" + target;
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return StrUtil.blankToDefault(ip, "unknown");
    }
}
