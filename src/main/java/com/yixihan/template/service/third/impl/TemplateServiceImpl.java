package com.yixihan.template.service.third.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.common.util.Panic;
import com.yixihan.template.mapper.third.TemplateMapper;
import com.yixihan.template.model.third.Template;
import com.yixihan.template.service.third.TemplateService;
import com.yixihan.template.common.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 模板表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Slf4j
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

    @Override
    public String getTemplateContent(String templateCode) {
        Assert.notBlank(templateCode);

        Template template = lambdaQuery()
                .eq(Template::getTemplateCode, templateCode)
                .one();

        if (ObjUtil.isNull(template)) {
            Panic.noSuchEntry(Template.class, templateCode);
        }
        return template.getContent();

    }
}
