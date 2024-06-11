package com.yixihan.template.vo.req.third;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * os-获取上传凭证-req
 *
 * @author yixihan
 * @date 2024-06-07 09:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "os-获取上传凭证-req")
public class OsCertificateReq {

    @Schema(description = "用户 id")
    private Long userId;

    @Schema(description = "目录")
    private String dir;

    @Schema(description = "文件名")
    private String key;

    @Schema(description = "详细信息(json 格式)")
    private String details;
}
