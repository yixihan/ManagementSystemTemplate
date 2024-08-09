package com.yixihan.template.common.builder;

import cn.hutool.core.lang.UUID;
import cn.hutool.extra.spring.SpringUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.Headers;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.qcloud.cos.utils.DateUtils;
import com.yixihan.template.config.third.OsConfig;
import com.yixihan.template.common.exception.BizException;
import com.yixihan.template.common.exception.InvalidParameterException;
import com.yixihan.template.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯云 COS builder
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/436/10199">COS Java SDK Doc</a>
 * <br/>
 * 仅实现了服务端简单上传、客户端直传与下载, 复杂功能请获取 CosClient 后自己实现
 *
 * @author yixihan
 * @date 2024-06-11 10:25
 */
@Slf4j
public class CosBuilder {

    /**
     * 是否返回凭证
     */
    private Boolean certificate = false;

    /**
     * 服务器直传
     */
    private Boolean directTransmission = true;

    /**
     * 获取 COS 对象
     */
    private Boolean cosClient = false;

    /**
     * 获取 TransferManager 对象
     */
    private Boolean transferManager = false;

    /**
     * 上传-文件
     */
    private MultipartFile file;

    /**
     * 保存文件名
     */
    private String key;


    private CosBuilder() {
    }

    public static CosBuilder build() {
        return new CosBuilder();
    }

    /**
     * 客户端上传凭证
     * <br/>
     * <a href="https://developer.qiniu.com/kodo/1239/java#upload-token">SDK Doc</a>
     */
    public CosBuilder certificate() {
        this.certificate = true;
        return this;
    }

    /**
     * 服务器直传
     * <br/>
     * <a href="https://developer.qiniu.com/kodo/1239/java#server-upload">SDK Doc</a>
     */
    public CosBuilder directTransmission() {
        this.directTransmission = true;
        return this;
    }

    /**
     * 获取 COSClient
     */
    public CosBuilder cosClient() {
        this.cosClient = true;
        return this;
    }

    /**
     * 获取 COSClient
     */
    public CosBuilder transferManager() {
        this.transferManager = true;
        return this;
    }

    /**
     * 设置需要上传的文件
     */
    public CosBuilder file(MultipartFile file) {
        this.file = file;
        return this;
    }

    /**
     * 设置保存文件名
     */
    public CosBuilder key(String key) {
        this.key = key;
        return this;
    }

    public Object done() {
        if (certificate) {
            return getCertificate(getCOSClient());
        } else if (directTransmission) {
            return uploadFile();
        } else if (cosClient) {
            return getCOSClient();
        } else if (transferManager) {
            return getTransferManager();
        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * 获取上传凭证
     * <br/>
     * <a href="https://cloud.tencent.com/document/product/436/35217">生成预签名 URL</a>
     */
    private URL getCertificate(COSClient cosClient) {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = SpringUtil.getBean(OsConfig.class).getBucketName();
        // 对象键(Key)是对象在存储桶中的唯一标识。详情请参见 [对象键](https://cloud.tencent.com/document/product/436/13324)
        String key = UUID.fastUUID().toString();

        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.PUT);

        // 设置下载时返回的 http 头
        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        String responseContentType = "image/x-icon";
        String responseContentLanguage = "zh-CN";
        // 设置返回头部里包含文件名信息
        String responseContentDispositon = "filename=exampleobject";
        String responseCacheControl = "no-cache";
        String cacheExpireStr = DateUtils.formatRFC822Date(new Date(System.currentTimeMillis() + 24L * 3600L * 1000L));
        responseHeaders.setContentType(responseContentType);
        responseHeaders.setContentLanguage(responseContentLanguage);
        responseHeaders.setContentDisposition(responseContentDispositon);
        responseHeaders.setCacheControl(responseCacheControl);
        responseHeaders.setExpires(cacheExpireStr);
        req.setResponseHeaders(responseHeaders);


        // 设置签名过期时间(可选)，若未进行设置，则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
        req.setExpiration(expirationDate);


        // 填写本次请求的参数
        req.addRequestParameter("param1", "value1");
        // 填写本次请求的头部
        // host 必填
        req.putCustomRequestHeader(Headers.HOST, cosClient.getClientConfig().getEndpointBuilder().buildGeneralApiEndpoint(bucketName));
        req.putCustomRequestHeader("header1", "value1");
        URL url = cosClient.generatePresignedUrl(req);

        // 确认本进程不再使用 cosClient 实例之后，关闭即可
        cosClient.shutdown();
        return url;
    }

    /**
     * 服务器直传
     * <br/>
     * <a href="https://cloud.tencent.com/document/product/436/65935">PUT Object</a>
     */
    private UploadResult uploadFile() {
        OsConfig config = SpringUtil.getBean(OsConfig.class);
        PutObjectRequest putObjectRequest = new PutObjectRequest(config.getBucketName(), key, FileUtil.convertToFile(file));

        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia) https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        //若需要设置对象的自定义 Headers 可参照下列代码,若不需要可省略下面这几行,对象自定义 Headers 的详细信息可参考 https://cloud.tencent.com/document/product/436/13361
        ObjectMetadata objectMetadata = new ObjectMetadata();

        //若设置 Content-Type、Cache-Control、Content-Disposition、Content-Encoding、Expires 这五个字自定义 Headers，推荐采用 objectMetadata.setHeader(), 自定义header尽量避免特殊字符，使用中文前请先手动对其进行URLEncode
        objectMetadata.setHeader("key", "value");
        //若要设置 “x-cos-meta-[自定义后缀]” 这样的自定义 Header，推荐采用
        Map<String, String> userMeta = new HashMap<>();
        userMeta.put("x-cos-meta-[自定义后缀]", "value");
        objectMetadata.setUserMetadata(userMeta);
        putObjectRequest.withMetadata(objectMetadata);


        try {
            // 高级接口会返回一个异步结果Upload, 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回 UploadResult, 失败抛出异常
            Upload upload = getTransferManager().upload(putObjectRequest);
            return upload.waitForUploadResult();
        } catch (Exception e) {
            log.error("文件上传服务出现异常!", e);
            throw new BizException(e);
        }
    }


    private COSClient getCOSClient() {
        return SpringUtil.getBean(COSClient.class);
    }


    private TransferManager getTransferManager() {
        return SpringUtil.getBean(TransferManager.class);
    }

}
