package com.yixihan.template.service.third.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.mapper.third.TemplateMapper;
import com.yixihan.template.model.third.Template;
import com.yixihan.template.service.third.TemplateService;
import com.yixihan.template.util.Assert;
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

        return lambdaQuery()
                .eq(Template::getTemplateCode, templateCode)
                .oneOpt()
                .orElse(new Template())
                .getContent();
    }
}
