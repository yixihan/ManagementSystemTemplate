package com.yixihan.template.service.third.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.common.builder.os.*;
import com.yixihan.template.config.third.OsConfig;
import com.yixihan.template.common.exception.BizException;
import com.yixihan.template.mapper.third.ObjectStorageMapper;
import com.yixihan.template.model.third.ObjectStorage;
import com.yixihan.template.service.third.ObjectStorageService;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.FileUtil;
import com.yixihan.template.common.util.UserUtil;
import com.yixihan.template.vo.req.third.OsCertificateReq;
import com.yixihan.template.vo.req.third.OsUploadReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 对象存储表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-06-04
 */
@Slf4j
@Service
public class ObjectStorageServiceImpl extends ServiceImpl<ObjectStorageMapper, ObjectStorage> implements ObjectStorageService {

    @Resource
    private OsConfig osConfig;

    @Override
    public ObjectStorage certificate(OsCertificateReq req) {
        // 初始化 os
        ObjectStorage os = initOs(req);
        os.setOsType(osConfig.getType().getValue());

        switch (osConfig.getType()) {
            case OSS -> {
                Object certificate = OssBuilder.build()
                        .certificate()
                        .dir(req.getDir())
                        .done();
                os.setMetadata(JSONUtil.toJsonStr(certificate));
            }
            case KODO -> {
                String certificate = KodoBuilder.build()
                        .certificate()
                        .key(req.getKey())
                        .done();
                os.setMetadata(JSONUtil.toJsonStr(certificate));
            }
            case COS -> {
                Object certificate = CosBuilder.build()
                        .certificate()
                        .done();
                os.setMetadata(JSONUtil.toJsonStr(certificate));
            }
            default -> throw new BizException(StrUtil.format("{} 不支持凭证上传", osConfig.getType().getDesc()));
        }

        save(os);
        return os;
    }

    @Override
    public void certificateCallback(ObjectStorage os) {
        Assert.notNull(os);
        Assert.notNull(os.getOsId());

        save(os);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long upload(OsUploadReq req) {
        // 初始化 os
        ObjectStorage os = initOs(req);

        // 上传文件
        os.setOsType(osConfig.getType().getValue());
        // 这里逻辑未经过自测, 且并不会使用如此多的 os client, 推荐仅保留自己选择的 os 即可
        switch (osConfig.getType()) {
            case OSS -> {
                Object data = OssBuilder.build()
                        .directTransmission()
                        .dir(req.getDir())
                        .key(req.getKey())
                        .file(req.getFile())
                        .done();
                os.setMetadata(JSONUtil.toJsonStr(data));
            }
            case KODO -> {
                String data = KodoBuilder.build()
                        .directTransmission()
                        .key(req.getKey())
                        .file(req.getFile())
                        .done();
                os.setMetadata(JSONUtil.toJsonStr(data));
            }
            case COS -> {
                Object data = CosBuilder.build()
                        .directTransmission()
                        .key(req.getKey())
                        .file(req.getFile())
                        .done();
                os.setMetadata(JSONUtil.toJsonStr(data));
            }
            case SMMS -> {
                String data = SmmsBuilder.build()
                        .upload()
                        .file(req.getFile())
                        .done();

                os.setMetadata(JSONUtil.toJsonStr(data));
            }
            case DB -> {
                // 数据库存储, 将数据 encode 后放入 os obj 中
                os.setOsData(FileUtil.encodeFileData(req.getFile()));
                os.setMetadata(null);
            }
            case LOCAL -> {
                Object data = LocalOsBuilder.build()
                        .upload()
                        .dir(req.getDir())
                        .key(req.getKey())
                        .done();
                os.setOsPath(data.toString());
            }
        }

        save(os);
        return os.getOsId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long uploadFile(MultipartFile file) {
        OsUploadReq req = OsUploadReq.builder()
                .dir(UserUtil.getLoginUser().getUser().getId().toString())
                .key(file.getName())
                .file(file)
                .build();
        return upload(req);
    }

    /**
     * os upload req to os obj
     * @param req os upload req
     * @return {@link ObjectStorage}
     */
    private ObjectStorage initOs(OsUploadReq req) {
        ObjectStorage os = new ObjectStorage();
        os.setOsName(req.getKey());
        os.setOsPath(req.getDir());
        os.setContentType(req.getFile().getContentType());
        os.setEncoding("UTF-8");
        return os;
    }

    /**
     * os certificate req to os obj
     * @param req os certificate req
     * @return {@link ObjectStorage}
     */
    private ObjectStorage initOs(OsCertificateReq req) {
        ObjectStorage os = new ObjectStorage();
        os.setOsName(req.getKey());
        os.setOsPath(req.getDir());
        os.setContentType(null);
        os.setOsData(null);
        os.setEncoding("UTF-8");
        return os;
    }
}
