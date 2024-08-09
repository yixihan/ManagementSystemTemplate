package com.yixihan.template.config.os;

import cn.hutool.system.SystemUtil;
import com.yixihan.template.config.third.OsConfig;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 对象存储(OS) 配置
 * <br>
 * 主要为注册各服务商的 client bean
 *
 * @author yixihan
 * @date 2024-08-09 20:46
 */
@Configuration
public class OsClientConfig {

    @Resource
    private OsConfig osConfig;

    /**
     * 创建腾讯云 CosClient 实例
     * <li>accessKey: 用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。可参见 <a href="https://cloud.tencent.com/document/product/598/37140">子账号密钥获取</a></li>
     * <li>secretKey: 用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。可参见 <a href="https://cloud.tencent.com/document/product/598/37140">子账号密钥获取</a></li>
     */
    @Bean
    public com.qcloud.cos.COSClient createCosClient() {
        // 1 初始化用户身份信息. SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String accessKey = osConfig.getAccessKey();
        String secretKey = osConfig.getSecretKey();
        com.qcloud.cos.auth.COSCredentials cred = new com.qcloud.cos.auth.BasicCOSCredentials(accessKey, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        com.qcloud.cos.region.Region region = new com.qcloud.cos.region.Region(osConfig.getRegion());
        com.qcloud.cos.ClientConfig clientConfig = new com.qcloud.cos.ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(com.qcloud.cos.http.HttpProtocol.https);
        // 3 生成 cos 客户端。
        return new com.qcloud.cos.COSClient(cred, clientConfig);
    }

    /**
     * 创建腾讯云 TransferManager 实例，这个实例用来后续调用高级接口<br>
     * <li>上传可参考: <a href="https://cloud.tencent.com/document/product/436/65935">TransferManager 上传文件 Java SDK Doc</a></li>
     * <li>下载可参考: <a href="https://cloud.tencent.com/document/product/436/65937">TransferManager 下载文件 Java SDK Doc</a></li>
     * <li>更多方法请参考: <a href="https://cloud.tencent.com/document/product/436/10199">COS Java SDK Doc</a></li>
     *
     */
    @Bean
    com.qcloud.cos.transfer.TransferManager createCosTransferManager(com.qcloud.cos.COSClient cosClient) {
        // 自定义线程池大小, 默认 cpu 线程 / 4
        ExecutorService threadPool = Executors.newFixedThreadPool(SystemUtil.getTotalThreadCount() / 4);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        com.qcloud.cos.transfer.TransferManager transferManager = new com.qcloud.cos.transfer.TransferManager(cosClient, threadPool);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别为 5MB 和 1MB
        com.qcloud.cos.transfer.TransferManagerConfiguration transferManagerConfiguration = new com.qcloud.cos.transfer.TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        return transferManager;
    }

    /**
     * 创建阿里云 OSSClient 实例
     * <li>上传可参考: <a href="https://help.aliyun.com/zh/oss/developer-reference/upload-objects-21">OSS 上传文件 Java SDK Doc</a></li>
     * <li>下载可参考: <a href="https://help.aliyun.com/zh/oss/developer-reference/download-objects-18">OSS 下载文件 Java SDK Doc</a></li>
     * <li>更多方法请参考: <a href="https://help.aliyun.com/zh/oss/developer-reference/preface-1">OSS Java SDK Doc</a></li>
     */
    @Bean
    public com.aliyun.oss.OSS createOosClient() {
        // client config. 更多配置参考 https://help.aliyun.com/zh/oss/developer-reference/initialization-3
        com.aliyun.oss.ClientBuilderConfiguration clientBuilderConfiguration = new com.aliyun.oss.ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(com.aliyun.oss.common.comm.SignVersion.V4);

        // client auth config. 更多配置参考 https://help.aliyun.com/zh/oss/developer-reference/oss-java-configure-access-credentials
        com.aliyun.oss.common.auth.DefaultCredentialProvider credentialProvider =
                new com.aliyun.oss.common.auth.DefaultCredentialProvider(osConfig.getAccessKey(), osConfig.getSecretKey());

        return com.aliyun.oss.OSSClientBuilder.create()
                .endpoint(osConfig.getEndpoint())
                .credentialsProvider(credentialProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(osConfig.getRegion())
                .build();
    }

    /**
     * 创建七牛云 Kodo Auth 实例 - 用于凭证上传/下载
     * <li>上传可参考: <a href="https://developer.qiniu.com/kodo/1239/java#5">Kodo 上传文件 Java SDK Doc</a></li>
     * <li>下载可参考: <a href="https://developer.qiniu.com/kodo/1239/java#6">Kodo 下载文件 Java SDK Doc</a></li>
     * <li>更多方法请参考: <a href="https://developer.qiniu.com/kodo/1239/java#1">Kodo Java SDK Doc</a></li>
     */
    @Bean
    public com.qiniu.util.Auth createKodoAuth() {
        return com.qiniu.util.Auth.create(osConfig.getAccessKey(), osConfig.getSecretKey());
    }

    /**
     * 创建七牛云 Kodo UploadManager 实例 - 用于服务器上传/下载
     * <li>上传可参考: <a href="https://developer.qiniu.com/kodo/1239/java#5">Kodo 上传文件 Java SDK Doc</a></li>
     * <li>下载可参考: <a href="https://developer.qiniu.com/kodo/1239/java#6">Kodo 下载文件 Java SDK Doc</a></li>
     * <li>更多方法请参考: <a href="https://developer.qiniu.com/kodo/1239/java#1">Kodo Java SDK Doc</a></li>
     */
    @Bean
    public com.qiniu.storage.UploadManager createKodoUploadManager() {
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(com.qiniu.storage.Region.region0());
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = com.qiniu.storage.Configuration.ResumableUploadAPIVersion.V2;
        // ...其他参数参考类注释
        return new com.qiniu.storage.UploadManager(cfg);
    }
}
