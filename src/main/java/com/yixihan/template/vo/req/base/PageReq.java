package com.yixihan.template.vo.req.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求-req
 *
 * @author yixihan
 * @date 2024-05-21 15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页请求-req")
public class PageReq {

    @Schema(description = "页码")
    private Long page = 0L;

    @Schema(description = "分页大小")
    private Long pageSize = 10L;

    @Schema(description = "是否搜索总数据条数")
    private Boolean searchCount = true;
}
