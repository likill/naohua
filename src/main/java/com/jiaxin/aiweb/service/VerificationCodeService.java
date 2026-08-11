package com.jiaxin.aiweb.service;

import com.jiaxin.aiweb.model.dto.user.VerificationCodeLoginRequest;
import com.jiaxin.aiweb.model.dto.user.VerificationCodeSendRequest;
import com.jiaxin.aiweb.model.vo.LoginUserVO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 统一验证码服务。
 */
public interface VerificationCodeService {

    /**
     * 发送登录/注册验证码。
     */
    void sendCode(VerificationCodeSendRequest verificationCodeSendRequest, HttpServletRequest request);

    /**
     * 使用验证码登录；账号不存在时自动注册。
     */
    LoginUserVO loginByCode(VerificationCodeLoginRequest verificationCodeLoginRequest, HttpServletRequest request);
}
