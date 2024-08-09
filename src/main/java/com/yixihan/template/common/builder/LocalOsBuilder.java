package com.yixihan.template.common.builder;

import cn.hutool.extra.spring.SpringUtil;
import com.yixihan.template.config.third.OsConfig;
import com.yixihan.template.common.exception.InvalidParameterException;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 本地存储 builder
 *
 * @author yixihan
 * @date 2024-06-07 13:37
 */
@SuppressWarnings("unused")
@Slf4j
public class LocalOsBuilder {

    /**
     * 选择上传文件
     */
    private Boolean upload;

    /**
     * 选择下载文件
     */
    private Boolean download;

    /**
     * 文件
     */
    private File file;

    /**
     * 文件名
     */
    private String key;

    /**
     * 目录
     */
    private String dir;

    private LocalOsBuilder() {
    }

    public static LocalOsBuilder build() {
        return new LocalOsBuilder();
    }

    /**
     * 上传文件
     */
    public LocalOsBuilder upload() {
        this.upload = true;
        return this;
    }

    /**
     * 下载文件
     */
    public LocalOsBuilder download() {
        this.download = true;
        return this;
    }

    /**
     * 选择上传的文件
     */
    public LocalOsBuilder file(MultipartFile file) {
        return file(FileUtil.convertToFile(file));
    }

    /**
     * 选择上传的文件
     */
    public LocalOsBuilder file(File file) {
        this.file = file;
        return this;
    }

    /**
     * 设置文件名
     */
    public LocalOsBuilder key(String key) {
        this.key = key;
        return this;
    }

    /**
     * 设置文件目录
     */
    public LocalOsBuilder dir(String dir) {
        this.dir = dir;
        return this;
    }

    public Object done() {
        if (upload) {
            return uploadFile();
        } else if (download) {
            return downloadFile();
        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * 上传文件
     *
     * @return file absolutePath
     */
    private String uploadFile() {
        File newFile = FileUtil.newFile(getFilePath());

        // base64 编码后写入文件
        String data = FileUtil.encodeFileData(file);
        FileUtil.writeString(data, newFile, StandardCharsets.UTF_8);

        return newFile.getAbsolutePath();
    }

    /**
     * 下载文件
     *
     * @return os data (byte[] array)
     */
    private Object downloadFile() {
        File loadFile = FileUtil.file(getFilePath());
        return FileUtil.decodeFileData(loadFile);
    }

    /**
     * 获取 local 保存路径
     */
    private String getFilePath() {
        Assert.notBlank(dir);
        Assert.notBlank(key);
        OsConfig config = SpringUtil.getBean(OsConfig.class);
        return config.getLocalPath() + "/" + dir + "/" + key;
    }

}
