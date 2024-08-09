package com.yixihan.template.util.builder;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.yixihan.template.config.third.OsConfig;
import com.yixihan.template.exception.BizException;
import com.yixihan.template.exception.InvalidParameterException;
import com.yixihan.template.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 阿里云 OSS builder
 * <br/>
 * <a href="https://help.aliyun.com/zh/oss/developer-reference/java/?spm=a2c4g.11186623.0.0.418c5b0fMJNrk7">OSS Java SDK Doc</a>
 * <br/>
 * 仅实现了服务端简单上传、客户端直传与下载, 复杂功能请获取 OssClient 后自己实现
 *
 * @author yixihan
 * @date 2024-06-06 11:03
 */
@SuppressWarnings("unused")
@Slf4j
public class OssBuilder {

    /**
     * 是否返回凭证
     */
    private Boolean certificate = false;

    /**
     * 服务器直传
     */
    private Boolean directTransmission = true;

    /**
     * 获取 OSS 对象
     */
    private Boolean ossClient = false;

    /**
     * 上传-文件
     */
    private MultipartFile file;

    /**
     * 上传-字节数组
     */
    private byte[] array;

    /**
     * 上传-数据流
     */
    private InputStream stream;

    /**
     * 上传-字符串
     */
    private String str;

    /**
     * 保存文件名
     */
    private String key;

    /**
     * 保存目录
     */
    private String dir;


    private OssBuilder() {
    }

    public static OssBuilder build() {
        return new OssBuilder();
    }

    /**
     * 客户端上传凭证
     * <br/>
     * <a href="https://developer.qiniu.com/kodo/1239/java#upload-token">SDK Doc</a>
     */
    public OssBuilder certificate() {
        this.certificate = true;
        return this;
    }

    /**
     * 服务器直传
     * <br/>
     * <a href="https://developer.qiniu.com/kodo/1239/java#server-upload">SDK Doc</a>
     */
    public OssBuilder directTransmission() {
        this.directTransmission = true;
        return this;
    }

    /**
     * 获取 OssClient
     */
    public OssBuilder ossClient() {
        this.ossClient = true;
        return this;
    }

    /**
     * 设置需要上传的文件
     */
    public OssBuilder file(MultipartFile file) {
        this.file = file;
        return this;
    }

    /**
     * 设置需要上传的字节数组
     */
    public OssBuilder array(byte[] array) {
        this.array = array;
        return this;
    }

    /**
     * 设置需要上传的数据流
     */
    public OssBuilder stream(InputStream stream) {
        this.stream = stream;
        return this;
    }

    /**
     * 设置需要上传的字符串
     */
    public OssBuilder str(String str) {
        this.str = str;
        return this;
    }

    /**
     * 设置保存文件名
     */
    public OssBuilder key(String key) {
        this.key = key;
        return this;
    }

    /**
     * 设置上床目录
     */
    public OssBuilder dir(String dir) {
        this.dir = dir;
        return this;
    }


    public Object done() {
        if (certificate) {
            return getCertificate(getOssClient());
        } else if (directTransmission) {
            return upload(getOssClient());
        } else if (ossClient) {
            return getOssClient();
        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * 设置上传凭证
     * <br/>
     * <a href="https://help.aliyun.com/zh/oss/use-cases/uploading-objects-to-oss-directly-from-clients/?spm=a2c4g.11186623.0.i56">客户端直传 SDK Doc</a>
     */
    private Map<String, String> getCertificate(OSS ossClient) {
        OsConfig config = SpringUtil.getBean(OsConfig.class);
        // host 的格式为 bucketName.endpoint
        String host = "https://" + config.getBucketName() + "." + config.getEndpoint();
        Map<String, String> respMap = new HashMap<>();
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<>();
            respMap.put("accessId", config.getAccessKey());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

        } catch (Exception e) {
            log.error("文件上传服务出现异常!", e);
        }
        return respMap;
    }

    /**
     * 服务端直传文件
     * <br/>
     * <a href="https://help.aliyun.com/zh/oss/developer-reference/upload-objects-21/?spm=a2c4g.11186623.0.0.3a7f13aaimePJd">服务端上传文件 SDK Doc</a>
     */
    private PutObjectResult upload(OSS ossClient) {
        PutObjectResult data = null;
        try {
            OsConfig config = SpringUtil.getBean(OsConfig.class);
            if (ObjUtil.isNotEmpty(file)) {
                data = uploadFile(ossClient, config);
            } else if (ObjUtil.isNotEmpty(array)) {
                data = uploadArray(ossClient, config, this.array);
            } else if (ObjUtil.isNotEmpty(stream)) {
                data = uploadStream(ossClient, config, stream);
            } else if (ObjUtil.isNotEmpty(str)) {
                data = uploadStr(ossClient, config);
            }
        } catch (Exception e) {
            log.error("OSS 文件上传出错: {}", e.getMessage());
            throw new BizException(e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return data;
    }

    /**
     * 服务端直传 - 上传文件
     */
    private PutObjectResult uploadFile(OSS ossClient, OsConfig config) throws IOException {
        return uploadStream(ossClient, config, file.getInputStream());
    }

    /**
     * 服务端直传 - 上传数据流
     */
    private PutObjectResult uploadStream(OSS ossClient, OsConfig config, InputStream stream) throws IOException {
        // 创建PutObjectRequest对象
        PutObjectRequest putObjectRequest = new PutObjectRequest(config.getBucketName(), getObjectName(), stream);

        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传。
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        // 关闭输入流
        stream.close();
        return result;
    }

    /**
     * 服务端直传 - 上传字节数组
     */
    private PutObjectResult uploadArray(OSS ossClient, OsConfig config, byte[] array) throws IOException {
        return uploadStream(ossClient, config, new ByteArrayInputStream(array));
    }

    /**
     * 服务端直传 - 上传字符串
     */
    private PutObjectResult uploadStr(OSS ossClient, OsConfig config) throws IOException {
        return uploadArray(ossClient, config, str.getBytes());
    }

    /**
     * objectName 为 dir + key,
     * <br/>
     * <a href="https://help.aliyun.com/zh/oss/user-guide/how-to-upload-directories-to-and-download-directories-from-oss">ObjectName Doc</a>
     */
    private String getObjectName() {
        Assert.notBlank(key);
        return StrUtil.nullToEmpty(dir) + "/" + key;
    }


    /**
     * 创建 oss 对象
     *
     * @return {@link OSS}
     */
    private OSS getOssClient() {
        OsConfig config = SpringUtil.getBean(OsConfig.class);

        // 创建OSSClient实例。
        return new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKey(), config.getSecretKey());
    }

}
