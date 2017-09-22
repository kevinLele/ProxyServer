package com.hq.CloudPlatform.ProxyServer.service;

import com.hq.CloudPlatform.ProxyServer.entity.ProxyInfo;
import com.hq.CloudPlatform.ProxyServer.exception.ServiceException;

/**
 * Created by Administrator on 9/22/2017.
 */
public interface IProxyInfoService extends IBaseService<ProxyInfo> {

    ProxyInfo findByKey(String key) throws ServiceException;

}
