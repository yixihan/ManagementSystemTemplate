package com.yixihan.template.service.third;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 图片生成 服务
 *
 * @author yixihan
 * @date 2024-05-29 10:37
 */
public interface PictureService {

    /**
     * 生成图片验证码
     *
     * @param uuid     uuid(唯一标识)
     * @param response response
     */
    void generateValidatePicture(String uuid, HttpServletResponse response);

    /**
     * 生成二维码
     *
     * @param url      url
     * @param response response
     */
    void generateQrPicture(String url, HttpServletResponse response);
}
