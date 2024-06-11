package com.yixihan.template.service.third;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yixihan.template.model.third.ObjectStorage;
import com.yixihan.template.vo.req.third.OsCertificateReq;
import com.yixihan.template.vo.req.third.OsUploadReq;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 对象存储表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-06-04
 */
public interface ObjectStorageService extends IService<ObjectStorage> {

    /**
     * 获取上传凭证
     *
     * @param req 请求参数
     * @return os
     */
    ObjectStorage certificate(OsCertificateReq req);

    /**
     * 获取上传凭证-回调
     * 更新 metadata
     *
     * @param os {@link ObjectStorage}
     */
    void certificateCallback(ObjectStorage os);

    /**
     * 上传文件
     *
     * @param req 请求参数
     * @return os id
     */
    Long upload(OsUploadReq req);

    /**
     * 上传文件
     *
     * @param file 文件
     * @return os id
     */
    Long uploadFile(MultipartFile file);

}
