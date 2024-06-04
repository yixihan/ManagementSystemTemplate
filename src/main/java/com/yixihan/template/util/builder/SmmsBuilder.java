package com.yixihan.template.util.builder;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yixihan.template.config.third.OsConfig;
import com.yixihan.template.exception.BizException;
import com.yixihan.template.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * SM.MS 图床 builder
 *
 * @author yixihan
 * @date 2024-06-04 14:15
 */
@Slf4j
@SuppressWarnings("unused")
public class SmmsBuilder {

    private MultipartFile file;

    private String token;

    private String hash;

    private Long page;

    private Boolean uploadFlag = false;

    private Boolean deleteFlag = false;

    private Boolean historyFlag = false;

    private static final String BASIC_URL = "https://sm.ms/api/v2";

    private static final String TOKEN_URL = "/token";

    private static final String PROFILE_URL = "/profile";

    private static final String UPLOAD_URL = "/upload";

    private static final String DELETE_URL = "/delete/{}";

    private static final String HISTORY_URL = "/upload_history";

    public SmmsBuilder build() {
        SmmsBuilder builder = new SmmsBuilder();
        builder.initToken();
        return builder;
    }

    public SmmsBuilder hash(String hash) {
        this.hash = hash;
        return this;
    }

    public SmmsBuilder file(MultipartFile file) {
        this.file = file;
        return this;
    }

    public SmmsBuilder page(Long page) {
        this.page = page;
        return this;
    }

    public SmmsBuilder upload() {
        this.uploadFlag = true;
        return this;
    }

    public SmmsBuilder delete() {
        this.deleteFlag = true;
        return this;
    }

    public SmmsBuilder history() {
        this.historyFlag = true;
        return this;
    }

    public String done() {
        Assert.notBlank(token, new BizException("SM.MS token failed to obtain"));
        if (uploadFlag) {
            Assert.notNull(file);
            return uploadFile();
        } else if (deleteFlag) {
            Assert.notBlank(hash);
            return deleteFile();
        } else if (historyFlag) {
            Assert.notNull(page);
            return uploadHistory();
        } else {
            // 默认,
            return getUserProfile();
        }
    }

    /**
     * 初始化 token 单例模式
     */
    private void initToken() {
        if (StrUtil.isBlank(token)) {
            synchronized (SmmsBuilder.class) {
                if (StrUtil.isBlank(token)) {
                    getApiToken();
                }
            }
        }
    }

    /**
     * 获取 token
     */
    private void getApiToken() {
        OsConfig config = SpringUtil.getBean(OsConfig.class);
        if (!config.smms()) {
            log.warn("os type is not SM.MS");
        }
        if (StrUtil.isNotBlank(config.getToken())) {
            token = config.getToken();
        }
        if (!ObjUtil.isAllNotEmpty(config.getUsername(), config.getPassword())) {
            log.error("SM.MS config not configured");
        }


        try {
            String jsonBody = JSONUtil.toJsonStr(MapUtil.builder()
                    .put("username", config.getUsername())
                    .put("password", config.getPassword())
                    .build());

            HttpResponse response = HttpRequest.post(BASIC_URL + TOKEN_URL)
                    .body(jsonBody)
                    .execute();

            if (ObjUtil.isNotNull(response) && response.isOk()) {
                JSONObject jsonResp = JSONUtil.parseObj(response.body());

                if (!jsonResp.getBool("success")) {
                    throw new BizException(jsonResp.getStr("message"));
                }

                token = jsonResp.getJSONObject("data").getStr("token");
            }
        } catch (Exception e) {
            log.error("SM.MS 获取 token 失败: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取 用户 profile
     *
     * @return json data
     */
    public String getUserProfile() {
        initToken();
        String profile = StrUtil.EMPTY;

        try {
            HttpResponse response = HttpRequest.post(BASIC_URL + PROFILE_URL)
                    .addHeaders(header())
                    .execute();

            if (ObjUtil.isNotNull(response) && response.isOk()) {
                JSONObject jsonResp = JSONUtil.parseObj(response.body());

                if (!jsonResp.getBool("success")) {
                    throw new BizException(jsonResp.getStr("message"));
                }

                profile = jsonResp.getJSONObject("data").toString();
                log.info("SM.MS user profile: {}", profile);
            }
        } catch (Exception e) {
            log.error("SM.MS 获取 user profile 失败: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return profile;
    }

    /**
     * 上传文件
     *
     * @return json data
     */
    private String uploadFile() {
        String data = StrUtil.EMPTY;
        try {
            HttpResponse response = HttpRequest.post(BASIC_URL + UPLOAD_URL)
                    .addHeaders(header())
                    .form("smfile", file)
                    .form("format", "json")
                    .execute();

            if (ObjUtil.isNotNull(response) && response.isOk()) {
                JSONObject jsonResp = JSONUtil.parseObj(response.body());

                if (!jsonResp.getBool("success")) {
                    throw new BizException(jsonResp.getStr("message"));
                }

                data = jsonResp.getJSONObject("data").toString();
                log.debug("SM.MS upload success: {}", data);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    /**
     * 删除文件
     *
     * @return json data
     */
    private String deleteFile() {
        String data = StrUtil.EMPTY;
        try {
            HttpResponse response = HttpRequest.get(StrUtil.format(BASIC_URL + DELETE_URL, hash))
                    .addHeaders(header())
                    .execute();

            if (ObjUtil.isNotNull(response) && response.isOk()) {
                JSONObject jsonResp = JSONUtil.parseObj(response.body());

                if (!jsonResp.getBool("success")) {
                    throw new BizException(jsonResp.getStr("message"));
                }

                data = jsonResp.getJSONObject("data").toString();
                log.debug("SM.MS delete success: {}", data);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    /**
     * 上传历史记录
     *
     * @return json data
     */
    private String uploadHistory() {
        String data = StrUtil.EMPTY;
        try {
            HttpResponse response = HttpRequest.get(BASIC_URL + HISTORY_URL)
                    .addHeaders(header())
                    .form("page", page)
                    .execute();

            if (ObjUtil.isNotNull(response) && response.isOk()) {
                JSONObject jsonResp = JSONUtil.parseObj(response.body());

                if (!jsonResp.getBool("success")) {
                    throw new BizException(jsonResp.getStr("message"));
                }

                data = jsonResp.getJSONArray("data").toString();
                log.debug("SM.MS history get success: {}", data);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    /**
     * 设置请求头
     *
     * @return header map
     */
    private Map<String, String> header() {
        return MapUtil
                .builder("Authorization", token)
                .put("Content-Type", "multipart/form-data")
                .build();
    }
}
