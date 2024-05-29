package com.yixihan.template.util.builder;

import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.enums.SmsSourceEnums;
import com.yixihan.template.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送构建类
 *
 * @author yixihan
 * @date 2024-05-29 10:44
 */
@Slf4j
public class SmsBuilder {

    private String toMobile;

    private String templateId;

    private Map<String, Object> params;

    private String source;

    private SmsBuilder() {

    }

    public static SmsBuilder build() {
        return new SmsBuilder();
    }

    public SmsBuilder toMobile(String mobile) {
        this.toMobile = mobile;
        return this;
    }

    public SmsBuilder templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public SmsBuilder params(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public SmsBuilder addParams(String key, String value) {
        if (ObjUtil.isNull(params)) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public SmsBuilder source(SmsSourceEnums source) {
        this.source = source.getValue();
        return this;
    }

    public void send() {
        // params validate
        Assert.notBlank(toMobile);
        Assert.notBlank(templateId);
        Assert.notBlank(source);

        // todo
    }
}
