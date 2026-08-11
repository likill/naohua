package com.jiaxin.aiweb.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * 验证码目标类型。
 */
@Getter
public enum VerificationCodeTypeEnum {

    PHONE("手机号", "phone"),
    EMAIL("邮箱", "email");

    private final String text;

    private final String value;

    VerificationCodeTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举。
     */
    public static VerificationCodeTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (VerificationCodeTypeEnum anEnum : VerificationCodeTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
