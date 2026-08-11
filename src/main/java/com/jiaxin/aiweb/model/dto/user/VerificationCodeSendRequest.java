package com.jiaxin.aiweb.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送验证码请求。
 */
@Data
public class VerificationCodeSendRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码类型：phone/email。
     */
    private String type;

    /**
     * 手机号或邮箱。
     */
    private String target;
}
