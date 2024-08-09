package com.yixihan.template.common.builder;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.yixihan.template.config.third.OsConfig;
import com.yixihan.template.common.exception.InvalidParameterException;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 七牛云 Kodo 图床 builder
 * <br/>
 * 文档: <a href="https://developer.qiniu.com/kodo/1239/java#1">七牛云 Kodo Java SDK Doc</a>
 *
 * @author yixihan
 * @date 2024-06-04 16:13
 */
@SuppressWarnings("unused")
@Slf4j
public class KodoBuilder {

    /**
     * 是否返回凭证
     */
    private Boolean certificate = false;

    /**
     * 服务器直传
     */
    private Boolean directTransmission = true;

    /**
     * 下载文件
     */
    private Boolean download = false;

    /**
     * 是否为私有空间
     */
    private Boolean privateSpace = true;

    /**
     * 是否用 https, 默认不用
     */
    private Boolean useHttps = false;

    /**
     * 文件
     */
    private MultipartFile file;

    /**
     * 字节数组
     */
    private byte[] array;

    /**
     * 数据流
     */
    private InputStream stream;

    /**
     * 保存文件名, 文件名相同则覆盖上传
     */
    private String key;

    /**
     * 自定义返回 json 格式内容
     * 可以使用七牛云支持的 <a href="https://developer.qiniu.com/kodo/manual/1235/vars#magicvar">魔法变量</a> 和 <a href="https://developer.qiniu.com/kodo/manual/1235/vars#xvar">自定义变量</a>。
     */
    private StringMap policy;

    /**
     * 上传自定义参数，自定义参数名称需要以 x:开头
     */
    private StringMap params;


    private KodoBuilder() {
    }

    public static KodoBuilder build() {
        KodoBuilder builder = new KodoBuilder();
        builder.policy = new StringMap();
        builder.params = new StringMap();
        return builder;
    }

    /**
     * 客户端上传凭证
     * <br/>
     * <a href="https://developer.qiniu.com/kodo/1239/java#upload-token">SDK Doc</a>
     */
    public KodoBuilder certificate() {
        this.certificate = true;
        return this;
    }

    /**
     * 服务器直传
     * <br/>
     * <a href="https://developer.qiniu.com/kodo/1239/java#server-upload">SDK Doc</a>
     */
    public KodoBuilder directTransmission() {
        this.directTransmission = true;
        return this;
    }

    /**
     * 下载文件-获取链接
     */
    public KodoBuilder download() {
        this.download = true;
        return this;
    }

    /**
     * 设为私有空间
     */
    public KodoBuilder privateSpace() {
        this.privateSpace = true;
        return this;
    }

    /**
     * 设为使用 https
     */
    public KodoBuilder userHttps() {
        this.useHttps = true;
        return this;
    }

    /**
     * 设置需要上传的文件
     */
    public KodoBuilder file(MultipartFile file) {
        this.file = file;
        return this;
    }

    /**
     * 设置需要上传的字节数组
     */
    public KodoBuilder array(byte[] array) {
        this.array = array;
        return this;
    }

    /**
     * 设置需要上传的数据流
     */
    public KodoBuilder stream(InputStream stream) {
        this.stream = stream;
        return this;
    }

    /**
     * 设置保存文件名
     */
    public KodoBuilder key(String key) {
        this.key = key;
        return this;
    }

    /**
     * 上传策略设定
     * <br/>
     * <a href="https://developer.qiniu.com/kodo/1206/put-policy">SDK Doc</a>
     */
    public KodoBuilder putPolicy(String key, Object value) {
        this.policy.put(key, value);
        return this;
    }

    /**
     * 上传参数设定
     */
    public KodoBuilder putParams(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public String done() {
        if (certificate) {
            return getToken();
        } else if (directTransmission) {
            return upload();
        } else if (download) {
            return downloadFileUrl();
        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * 获取上传 Region
     * <p>
     * 机房   Region
     * <li>华东   Region.region0(), Region.huadong()</li>
     * <li>华北   Region.region1(), Region.huabei()</li>
     * <li>华南   Region.region2(), Region.huanan()</li>
     * <li>北美   Region.regionNa0(), Region.beimei()</li>
     * <li>东南亚  Region.regionAs0(), Region.xinjiapo()</li>
     * <br/>
     * <a href="https://developer.qiniu.com/kodo/1239/java#upload-config">SDK Doc</a>
     */
    private UploadManager getUploadManager() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        // 设置请求协议为http
        cfg.useHttpsDomains = useHttps;

        //...其他参数参考类注释

        return new UploadManager(cfg);
    }

    /**
     * 获取上传 token
     *
     * @return 上传 token
     */
    private String getToken() {
        if (StrUtil.isBlank(key)) {
            if (ObjUtil.isNotNull(file)) {
                key = file.getName();
            } else {
                key = UUID.fastUUID().toString();
            }
        }
        Assert.notBlank(key);
        OsConfig config = SpringUtil.getBean(OsConfig.class);
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        return auth.uploadToken(config.getBucketName(), key, 3600, policy);
    }

    /**
     * 服务端直传文件
     *
     * @return json data
     */
    private String upload() {
        Assert.notNull(file);
        if (StrUtil.isBlank(key)) {
            key = file.getName();
        }
        Assert.notBlank(key);

        UploadManager uploadManager = getUploadManager();
        String upToken = getToken();

        if (ObjUtil.isNotNull(file)) {
            return uploadFile(uploadManager, upToken);
        } else if (ObjUtil.isNotEmpty(array)) {
            return uploadArray(uploadManager, upToken);
        } else if (ObjUtil.isNotNull(stream)) {
            return uploadStream(uploadManager, upToken);
        } else {
            return StrUtil.EMPTY_JSON;
        }
    }

    /**
     * 文件上传
     *
     * @param uploadManager uploadManager
     * @param upToken       upToken
     * @return json data
     */
    private String uploadFile(UploadManager uploadManager, String upToken) {
        String data = StrUtil.EMPTY_JSON;

        try {
            Response response = uploadManager.put(FileUtil.convertToFile(file), key, upToken);
            //解析上传成功的结果
            data = response.bodyString();
        } catch (QiniuException ex) {
            log.error("七牛云 Kodo 上传文件错误: {}", ex.getMessage());
        }

        return data;
    }

    /**
     * 字节数组上传
     *
     * @param uploadManager uploadManager
     * @param upToken       upToken
     * @return json data
     */
    private String uploadArray(UploadManager uploadManager, String upToken) {
        String data = StrUtil.EMPTY_JSON;

        try {
            Response response = uploadManager.put(array, key, upToken);
            //解析上传成功的结果
            data = response.bodyString();
        } catch (QiniuException ex) {
            log.error("七牛云 Kodo 上传字节数组错误: {}", ex.getMessage());
        }

        return data;
    }

    /**
     * 数据流上传
     *
     * @param uploadManager uploadManager
     * @param upToken       upToken
     * @return json data
     */
    private String uploadStream(UploadManager uploadManager, String upToken) {
        String data = StrUtil.EMPTY_JSON;

        try {
            Response response = uploadManager.put(stream, key, upToken, null, null);
            //解析上传成功的结果
            data = response.bodyString();
        } catch (QiniuException ex) {
            log.error("七牛云 Kodo 上传数据流错误: {}", ex.getMessage());
        }

        return data;
    }

    /**
     * 获取下载文件链接
     *
     * @return download url
     */
    private String downloadFileUrl() {
        String data = StrUtil.EMPTY;
        try {
            OsConfig config = SpringUtil.getBean(OsConfig.class);
            DownloadUrl url = new DownloadUrl(config.getHost(), useHttps, key);
            // 自定义配置 ...

            if (privateSpace) {
                Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());

                // 带有效期
                long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
                long deadline = System.currentTimeMillis() / 1000 + expireInSeconds;

                data = url.buildURL(auth, deadline);
            }
            data = url.buildURL();
        } catch (QiniuException e) {
            log.error("七牛云 Kodo 获取下载链接错误: {}", e.getMessage());
        }

        return data;
    }
}
