package com.yixihan.template.vo.resp.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * auth vo
 *
 * @author yixihan
 * @date 2024-05-23 14:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "auth vo")
public class AuthVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5846264983053073886L;

    @Schema(description = "用户信息")
    private UserVO user;

    @Schema(description = "角色信息")
    private List<RoleVO> roleList;

    @Schema(description = "token")
    private String token;
}
