package com.jiaxin.aiweb.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码登录/注册请求。
 */
@Data
public class VerificationCodeLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码类型：phone/email。
     */
    private String type;

    /**
     * 手机号或邮箱。
     */
    private String target;

    /**
     * 6 位验证码。
     */
    private String code;
}
