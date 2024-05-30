package com.yixihan.template.service.third.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yixihan.template.config.third.CodeConfig;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.exception.PictureException;
import com.yixihan.template.service.third.CodeService;
import com.yixihan.template.service.third.PictureService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 图片生成 服务
 *
 * @author yixihan
 * @date 2024-05-29 10:37
 */
@Slf4j
@Service
public class PictureServiceImpl implements PictureService {

    @Resource
    private CodeConfig codeConfig;

    @Resource
    private CodeService codeService;

    @Override
    public void generateValidatePicture(String uuid, HttpServletResponse response) {
        try {
            // 生成图片验证码
            CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, codeConfig.getLen(), 20);

            // 获取验证码
            String code = captcha.getCode();

            // 获取 redis key
            String keyName = StrUtil.format(codeConfig.getCommonKey(), uuid);

            // 存入 redis
            codeService.addRedis(keyName, code);

            //图形验证码写出到响应体
            captcha.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("图片验证码服务出现异常!", e);
            throw new PictureException(ExceptionEnums.PICTURE_CODE_ERR);
        }
    }

    @Override
    public void generateQrPicture(String url, HttpServletResponse response) {
        // 生成指定url对应的二维码到文件，宽和高都是300像素
        try {
            QrConfig config = QrConfig.create()
                    .setWidth(300)
                    .setHeight(300)
                    .setErrorCorrection(ErrorCorrectionLevel.H);

            QrCodeUtil.generate(
                    url,
                    config,
                    ImgUtil.IMAGE_TYPE_PNG,
                    response.getOutputStream()
            );
        } catch (IOException e) {
            log.error("二维码服务出现异常!", e);
            throw new PictureException(ExceptionEnums.PICTURE_QR_ERR);
        }
    }
}
