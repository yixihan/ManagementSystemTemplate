package com.yixihan.template.service.third;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yixihan.template.model.third.Template;

/**
 * <p>
 * 模板表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
public interface TemplateService extends IService<Template> {

    /**
     * 获取模板内容
     *
     * @param templateCode 模板 code
     * @return 模板 content
     */
    String getTemplateContent(String templateCode);
}
