package com.hq.CloudPlatform.ProxyServer.restful.impl;

import com.hq.CloudPlatform.ProxyServer.exception.ServiceException;
import com.hq.CloudPlatform.ProxyServer.restful.IProxyRestService;
import com.hq.CloudPlatform.ProxyServer.service.IProxyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path("proxy")
@Component
public class ProxyRestServiceImpl implements IProxyRestService {

    @Autowired
    @Qualifier("proxyInfoService")
    private IProxyInfoService proxyInfoService;

    @Override
    public String register(String url, String type) throws ServiceException {
        return null;
    }
}
