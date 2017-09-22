package com.hq.CloudPlatform.ProxyServer.service.impl;

import com.hq.CloudPlatform.ProxyServer.entity.ProxyInfo;
import com.hq.CloudPlatform.ProxyServer.exception.ServiceException;
import com.hq.CloudPlatform.ProxyServer.mapper.BaseMapper;
import com.hq.CloudPlatform.ProxyServer.mapper.ProxyInfoMapper;
import com.hq.CloudPlatform.ProxyServer.service.IProxyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 9/22/2017.
 */
@Service("proxyInfoService")
public class ProxyInfoServiceImpl extends BaseServiceImpl<ProxyInfo> implements IProxyInfoService {

    @Autowired
    @Qualifier("proxyInfoMapper")
    private ProxyInfoMapper proxyInfoMapper;

    @Override
    public BaseMapper<ProxyInfo> getBaseMapper() {
        return proxyInfoMapper;
    }

    @Override
    public ProxyInfo findByKey(String key) throws ServiceException {
        ProxyInfo entity;

        try {
            entity = proxyInfoMapper.findByKey(key);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return entity;
    }
}
