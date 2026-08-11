package com.jiaxin.aiweb.service.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jiaxin.aiweb.config.AuthCodeProperties;
import com.jiaxin.aiweb.exception.BusinessException;
import com.jiaxin.aiweb.exception.ErrorCode;
import com.jiaxin.aiweb.model.enums.VerificationCodeTypeEnum;
import com.jiaxin.aiweb.service.VerificationCodeSender;

/**
 * 邮箱验证码发送器，推荐接阿里云邮件推送（DirectMail）SMTP。
 */
@Service
public class MailVerificationCodeSender implements VerificationCodeSender {

    @Resource
    private AuthCodeProperties authCodeProperties;

    @Resource
    private ObjectProvider<JavaMailSender> javaMailSenderProvider;

    @Override
    public VerificationCodeTypeEnum getType() {
        return VerificationCodeTypeEnum.EMAIL;
    }

    @Override
    public void send(String target, String code) {
        JavaMailSender javaMailSender = javaMailSenderProvider.getIfAvailable();
        AuthCodeProperties.Mail mail = authCodeProperties.getMail();
        if (javaMailSender == null || mail == null || StrUtil.isBlank(mail.getFrom())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "邮件服务未配置");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail.getFrom());
        message.setTo(target);
        message.setSubject(StrUtil.blankToDefault(mail.getSubject(), "脑花登录验证码"));
        message.setText("您的验证码是：" + code + "，5 分钟内有效。若非本人操作，请忽略本邮件。");
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "邮箱验证码发送失败");
        }
    }
}
