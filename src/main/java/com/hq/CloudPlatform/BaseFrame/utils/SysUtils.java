package com.hq.CloudPlatform.BaseFrame.utils;

import com.hq.CloudPlatform.BaseFrame.sys.Constants;
import com.hq.CloudPlatform.BaseFrame.utils.rest.WebTargetProvider;

import javax.ws.rs.client.WebTarget;

/**
 * Created by Administrator on 7/10/2017.
 */
public class SysUtils {

    public static WebTarget getCAWebTarget() {
        WebTarget client = WebTargetProvider.getWebResource(Constants.CA_IP,
                Constants.CA_PORT, Constants.CA_SERVER);

        return client;
    }
}
