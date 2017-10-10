package com.hq.CloudPlatform.ProxyServer.restful.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hq.CloudPlatform.ProxyServer.entity.ProxyInfo;
import com.hq.CloudPlatform.ProxyServer.exception.ServiceException;
import com.hq.CloudPlatform.ProxyServer.restful.IProxyRestService;
import com.hq.CloudPlatform.ProxyServer.service.IProxyInfoService;
import com.hq.CloudPlatform.ProxyServer.utils.ConfigHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path("proxy")
@Component
@Slf4j
public class ProxyRestServiceImpl implements IProxyRestService {

    @Autowired
    @Qualifier("proxyInfoService")
    private IProxyInfoService proxyInfoService;

    private static final String proxyIp = ConfigHelper.getValue("ProxyServer.ip");

    private static final String proxyPort = ConfigHelper.getValue("ProxyServer.port");

    private static final String protocol = ConfigHelper.getValue("ProxyServer.protocol");

    private static final String prefix = ConfigHelper.getValue("ProxyServer.prefix");

    @Override
    public String register(String url, String type) throws ServiceException {
        String key = DigestUtils.md5Hex(url);
        ProxyInfo entity = findByKey(key);

        if (null == entity) {
            StringBuffer proxyUrlSB = new StringBuffer();
            proxyUrlSB.append(protocol).append("://")
                    .append(proxyIp).append(":")
                    .append(proxyPort).append("/")
                    .append(prefix).append("/")
                    .append(type).append("/")
                    .append(key);

            entity = new ProxyInfo();
            entity.setOriginalUrl(url);
            entity.setType(type);
            entity.setProxyUrl(proxyUrlSB.toString());
            entity.setUrlKey(key);
            long id = proxyInfoService.save(entity);
            entity.setId(id);
        }

        return JSON.toJSONString(entity);
    }

    @Override
    public String register(String jsonStr) throws ServiceException {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String url = jsonObject.getString("url");
        String type = jsonObject.getString("type");

        return register(url, type);
    }

    private ProxyInfo findByKey(String key) {
        try {
            return proxyInfoService.findByKey(key);
        } catch (ServiceException e) {
            log.error("Finding by Key error!", e);
        }

        return null;
    }
}
