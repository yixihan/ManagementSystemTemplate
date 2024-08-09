package com.yixihan.template.common.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.yixihan.template.common.constant.NumberConstant;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5 工具
 * <br/>
 * 用于哈希加密密码
 *
 * @author yixihan
 * @date 2024-05-21 15:20
 */
@SuppressWarnings("unused")
public class MD5Util {

    private MD5Util() {
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b byte数组
     * @return 16进制字符串
     */
    public static String byteToHexString(byte[] b) {
        StringBuilder hexString = new StringBuilder();
        for (byte value : b) {
            String hex = Integer.toHexString(value & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    public static String generateSalt() {
        final String randomStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        return RandomUtil.randomString(randomStr, NumberConstant.NUM_10);
    }

    /**
     * 获得加密后的16进制形式口令
     *
     * @param password 密码
     * @param salt     盐
     * @return 加密后的密码
     */
    @SneakyThrows
    public static String md5(String password, String salt) {
        //声明加密后的口令数组变量
        byte[] pwd;

        //声明消息摘要对象
        MessageDigest md;
        //创建消息摘要
        md = MessageDigest.getInstance("MD5");
        //将盐数据传入消息摘要对象
        md.update(StrUtil.bytes(salt, StandardCharsets.UTF_8));
        //将口令的数据传给消息摘要对象
        md.update(StrUtil.bytes(password, StandardCharsets.UTF_8));
        //获得消息摘要的字节数组
        byte[] digest = md.digest();

        //因为要在口令的字节数组中存放盐，所以加上盐的字节长度
        pwd = new byte[digest.length];
        //将消息摘要拷贝到加密口令字节数组
        System.arraycopy(digest, 0, pwd, 0, digest.length);
        //将字节数组格式加密后的口令转化为16进制字符串格式的口令
        return byteToHexString(pwd);
    }
}
