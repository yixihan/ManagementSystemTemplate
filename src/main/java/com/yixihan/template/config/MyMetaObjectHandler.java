package com.yixihan.template.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyBatis Plus 自动注入 配置
 *
 * @author yixihan
 * @date 2024-05-21 11:59
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        strictInsertFill(metaObject, "createDate", Date.class, new Date());
        strictInsertFill(metaObject, "updateDate", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updateDate", Date.class, new Date());
    }
}