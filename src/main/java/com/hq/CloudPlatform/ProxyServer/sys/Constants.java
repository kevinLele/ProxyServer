package com.hq.CloudPlatform.ProxyServer.sys;

import com.hq.CloudPlatform.ProxyServer.utils.ConfigHelper;

public interface Constants {

    String APP_CODE = ConfigHelper.getValue("sys.appCode");

    String CA_IP = ConfigHelper.getValue("ca.ip");

    String CA_PORT = ConfigHelper.getValue("ca.port");

    String CA_SERVER = ConfigHelper.getValue("ca.server");

    String SESSION_KEY_USER = "SESSION_USER";

    interface Caches {

        String DICTIONARY_CACHE = "dictionary";

        String CACHE_NAME = "default";

        String CFG_CACHE_NAME = "config";
    }

    interface MediaType {

        String APPLICATION_JSON = javax.ws.rs.core.MediaType.APPLICATION_JSON
                + ";charset=utf-8";

    }

    /**
     * Restful 对外的静态变量
     */
    interface JsonView {

        /**
         * 成功
         */
        String STATUS_SUCCESS = "success";

        /**
         * 失败
         */
        String STATUS_FAIL = "fail";

        /**
         * 未认证（即未登陆系统）
         */
        String UNAUTHENTICATED = "unauthenticated";

        /**
         * 未授权(即登陆成功但没有相关操作权限)
         */
        String UNAUTHORIZED = "unauthorized";

    }
}
