package com.yixihan.template.config.os;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.system.SystemUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.yixihan.template.config.third.OsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description
 *
 * @author yixihan
 * @date 2024-06-11 10:56
 */
@Configuration
public class CosConfig {

    /**
     * 获取 CosClient
     * <li>accessKey: 用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。可参见 <a href="https://cloud.tencent.com/document/product/598/37140">子账号密钥获取</a></li>
     * <li>secretKey: 用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。可参见 <a href="https://cloud.tencent.com/document/product/598/37140">子账号密钥获取</a></li>
     */
    @Bean
    public COSClient createCosClient() {
        OsConfig config = SpringUtil.getBean(OsConfig.class);
        // 1 初始化用户身份信息. SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String accessKey = config.getAccessKey();
        String secretKey = config.getSecretKey();
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(config.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    /**
     * 创建 TransferManager 实例，这个实例用来后续调用高级接口
     */
    @Bean
    TransferManager createTransferManager(COSClient cosClient) {
        // 自定义线程池大小
        ExecutorService threadPool = Executors.newFixedThreadPool(SystemUtil.getTotalThreadCount());

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别为 5MB 和 1MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        return transferManager;
    }
}
