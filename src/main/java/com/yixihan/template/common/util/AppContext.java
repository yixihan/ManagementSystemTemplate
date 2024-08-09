package com.yixihan.template.common.util;

import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.vo.resp.user.AuthVO;
import lombok.Setter;


/**
 * 应用程序上下文 工具
 *
 * @author yixihan
 * @date 2024-05-26 11:41
 */
@Setter
@SuppressWarnings("unused")
public class AppContext {

    private static final ThreadLocal<AppContext> appContextHolder = new ThreadLocal<>();

    private AuthVO loginUser;


    private AppContext() {
    }

    public static AppContext getInstance() {
        AppContext appContext = appContextHolder.get();
        if (ObjUtil.isNull(appContext)) {
            synchronized (appContextHolder) {
                appContext = appContextHolder.get();
                if (ObjUtil.isNull(appContext)) {
                    appContext = new AppContext();
                    appContextHolder.set(appContext);
                }
            }
        }
        return appContext;
    }

    public static void cleanContext() {
        appContextHolder.remove();
    }

    public AuthVO getLoginUser() {
        if (loginUser == null) {
            Panic.noAuth("You have not logged in");
        }
        return loginUser;
    }
}
