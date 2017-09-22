package com.hq.CloudPlatform.ProxyServer.entity;

import lombok.Data;

/**
 * Created by Administrator on 9/22/2017.
 */
@Data
public class ProxyInfo extends BaseEntity {

    private String originalUrl;

    private String proxyUrl;

    private String type;
}
