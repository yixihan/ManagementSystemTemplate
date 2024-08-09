package com.yixihan.template.vo.req.user;

import com.yixihan.template.vo.req.base.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户查询参数
 *
 * @author yixihan
 * @date 2024-05-28 14:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户登录参数")
public class UserQueryReq extends PageReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 8849313937678696904L;

    @Schema(description = "query key")
    private String queryKey;

    @Schema(description = "角色列表")
    private List<Long> roleIdList;
}
