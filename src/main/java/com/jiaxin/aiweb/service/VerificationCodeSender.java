package com.jiaxin.aiweb.service;

import com.jiaxin.aiweb.model.enums.VerificationCodeTypeEnum;

/**
 * 验证码发送通道。
 */
public interface VerificationCodeSender {

    /**
     * 支持的验证码类型。
     */
    VerificationCodeTypeEnum getType();

    /**
     * 发送验证码。
     */
    void send(String target, String code);
}
