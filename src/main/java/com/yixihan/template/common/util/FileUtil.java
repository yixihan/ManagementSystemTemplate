package com.yixihan.template.common.util;

import cn.hutool.core.codec.Base64;
import com.yixihan.template.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 文件工具
 *
 * @author yixihan
 * @date 2024-06-07 13:38
 */
@Slf4j
public class FileUtil extends cn.hutool.core.io.FileUtil {

    private FileUtil() {
    }

    /**
     * 将 MultipartFile 转换为 File 对象
     *
     * @param multipartFile MultipartFile实例
     * @return 转换后的File对象
     */
    public static File convertToFile(MultipartFile multipartFile) {
        try {
            // 生成一个临时文件路径
            Path tempFilePath = Files.createTempFile("temp", multipartFile.getOriginalFilename());

            // 将multipartFile内容写入临时文件
            try (FileOutputStream fos = new FileOutputStream(tempFilePath.toFile())) {
                fos.write(multipartFile.getBytes());
            }

            return tempFilePath.toFile();
        } catch (IOException e) {
            log.error("convertToFile error: {}", e.getMessage());
            throw new BizException(e);
        }
    }

    public static String encodeFileData(File file) {
        return Base64.encode(getInputStream(file));
    }

    public static String encodeFileData(MultipartFile file) {
        try {
            return Base64.encode(file.getInputStream());
        } catch (IOException e) {
            throw new BizException(e);
        }
    }

    public static byte[] decodeFileData(String encode) {
        return Base64.decode(encode);
    }

    public static byte[] decodeFileData(File file) {
        return decodeFileData(readFile(file));
    }

    public static String readFile(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new BizException(e);
        }
        return contentBuilder.toString();
    }
}
