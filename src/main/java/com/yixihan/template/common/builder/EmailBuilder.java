package com.yixihan.template.common.builder;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yixihan.template.config.third.EmailConfig;
import com.yixihan.template.common.enums.ExceptionEnums;
import com.yixihan.template.common.exception.BizException;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.ValidationUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 邮件发送 builder
 *
 * @author yixihan
 * @date 2024-05-29 13:56
 */
@Slf4j
public class EmailBuilder {

    private String toEmail;

    private String fromEmail;

    private String subject;

    private String content;

    private EmailBuilder() {
    }

    public static EmailBuilder build() {
        return new EmailBuilder();
    }

    public EmailBuilder content(String format, Object... params) {
        this.content = StrUtil.format(format, params);
        return this;
    }

    public EmailBuilder toEmail(String toEmail) {
        this.toEmail = toEmail;
        return this;
    }

    public EmailBuilder fromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
        return this;
    }

    public EmailBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void send() {
        try {
            // default value fill
            EmailConfig emailConfig = SpringUtil.getBean(EmailConfig.class);
            if (StrUtil.isBlank(fromEmail)) {
                fromEmail = emailConfig.getSendEmail();
            }
            if (StrUtil.isBlank(subject)) {
                subject = emailConfig.getTitle();
            }

            // validate params
            Assert.notBlank(toEmail);
            Assert.notBlank(fromEmail);
            Assert.isTrue(ValidationUtil.validateEmail(toEmail));
            Assert.isTrue(ValidationUtil.validateEmail(fromEmail));

            JavaMailSender mailSender = SpringUtil.getBean(JavaMailSender.class);
            MimeMessage mailMessage = mailSender.createMimeMessage();
            // 组装邮件
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "utf-8");
            helper.setSubject(subject);
            helper.setText(content, true);
            // 收件人
            helper.setTo(toEmail);
            // 发件人
            helper.setFrom(fromEmail);
            // 发送
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            log.error("邮件发送失败", e);
            throw new BizException(ExceptionEnums.EMAIL_SEND_ERR);
        }
    }

}
