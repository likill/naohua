package com.jiaxin.aiweb.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.jiaxin.aiweb.config.AuthCodeProperties;
import com.jiaxin.aiweb.exception.BusinessException;
import com.jiaxin.aiweb.exception.ErrorCode;
import com.jiaxin.aiweb.model.enums.VerificationCodeTypeEnum;
import com.jiaxin.aiweb.service.VerificationCodeSender;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 阿里云短信验证码发送器。
 */
@Service
public class AliyunSmsVerificationCodeSender implements VerificationCodeSender {

    @Resource
    private AuthCodeProperties authCodeProperties;

    @Override
    public VerificationCodeTypeEnum getType() {
        return VerificationCodeTypeEnum.PHONE;
    }

    @Override
    public void send(String target, String code) {
        AuthCodeProperties.Sms sms = authCodeProperties.getSms();
        if (sms == null || StrUtil.hasBlank(sms.getAccessKeyId(), sms.getAccessKeySecret(),
                sms.getSignName(), sms.getTemplateCode())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "短信服务未配置");
        }
        try {
            DefaultProfile profile = DefaultProfile.getProfile(
                    sms.getRegionId(),
                    sms.getAccessKeyId(),
                    sms.getAccessKeySecret()
            );
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");
            request.putQueryParameter("PhoneNumbers", formatPhoneNumber(target));
            request.putQueryParameter("SignName", sms.getSignName());
            request.putQueryParameter("TemplateCode", sms.getTemplateCode());
            request.putQueryParameter("TemplateParam", JSONUtil.toJsonStr(Map.of("code", code)));
            CommonResponse response = client.getCommonResponse(request);
            String responseCode = JSONUtil.parseObj(response.getData()).getStr("Code");
            if (!"OK".equals(responseCode)) {
                String responseMessage = JSONUtil.parseObj(response.getData()).getStr("Message");
                throw new BusinessException(ErrorCode.OPERATION_ERROR,
                        "短信验证码发送失败：" + StrUtil.blankToDefault(responseMessage, responseCode));
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "短信验证码发送失败");
        }
    }

    private String formatPhoneNumber(String phone) {
        if (phone.startsWith("+86")) {
            return phone.substring(3);
        }
        if (phone.startsWith("+")) {
            return phone.substring(1);
        }
        return phone;
    }
}
