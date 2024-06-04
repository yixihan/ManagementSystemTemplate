package com.yixihan.template.model.third;

import java.io.Serial;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 对象存储表
 *
 * @author yixihan
 * @since 2024-06-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "对象存储表")
public class ObjectStorage implements Serializable {

    @Serial
    private static final long serialVersionUID = 4663702293704660825L;

    @Schema(description = "os data")
    private String osData;

    @Schema(description = "os name")
    private String osName;

    @Schema(description = "os 存储路径")
    private String osPath;

    @Schema(description = "content type")
    private String contentType;

    @Schema(description = "编码")
    private String encoding;

    @Schema(description = "存储类型")
    private String osType;

    public static final String ID = "id";

    public static final String OS_DATA = "os_data";

    public static final String OS_NAME = "os_name";

    public static final String CONTENT_TYPE = "content_type";

    public static final String ENCODING = "encoding";

    public static final String OS_TYPE = "os_type";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";

}
