package com.yixihan.template.vo.resp.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页请求-vo
 *
 * @author yixihan
 * @date 2024-05-21 15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页请求-vo")
public class PageVO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2763957656843891790L;

    @Schema(description = "当前页")
    private Long current;

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "每页展示条数")
    private Long size;

    @Schema(description = "pages")
    private Long pages;

    @Schema(description = "数据")
    private List<T> records;
}