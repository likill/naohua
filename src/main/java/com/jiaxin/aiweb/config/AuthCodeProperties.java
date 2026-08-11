package com.jiaxin.aiweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 统一验证码配置。
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth.code")
public class AuthCodeProperties {

    /**
     * 验证码有效期（分钟）。
     */
    private int expireMinutes = 5;

    /**
     * 同一目标重复发送间隔（秒）。
     */
    private int resendIntervalSeconds = 60;

    /**
     * 同一手机号/邮箱每日最多发送次数。
     */
    private int dailyTargetLimit = 10;

    /**
     * 同一 IP 每小时最多发送次数。
     */
    private int hourlyIpLimit = 60;

    /**
     * 连续错误次数上限。
     */
    private int maxFailCount = 5;

    /**
     * 错误次数达到上限后的锁定时间（分钟）。
     */
    private int failLockMinutes = 15;

    private Sms sms = new Sms();

    private Mail mail = new Mail();

    @Data
    public static class Sms {

        /**
         * 阿里云 AccessKey ID。
         */
        private String accessKeyId;

        /**
         * 阿里云 AccessKey Secret。
         */
        private String accessKeySecret;

        /**
         * 阿里云地域。
         */
        private String regionId = "cn-hangzhou";

        /**
         * 短信签名。
         */
        private String signName;

        /**
         * 短信模板 Code，模板变量需包含 ${code}。
         */
        private String templateCode;
    }

    @Data
    public static class Mail {

        /**
         * 发信地址。
         */
        private String from;

        /**
         * 邮件标题。
         */
        private String subject = "脑花登录验证码";
    }
}
