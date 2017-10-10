package com.hq.CloudPlatform.ProxyServer.service;

import com.hq.CloudPlatform.ProxyServer.exception.ServiceException;
import com.hq.CloudPlatform.ProxyServer.restful.view.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/3/7.
 */
public interface IBaseService<Entity> {

    Page findByPage(Page<Entity> page, String countMapperFunc, String pageMapperFunc) throws ServiceException;

    long save(Entity entity) throws ServiceException;

    boolean update(Entity entity) throws ServiceException;

    boolean batchUpdate(Entity entity, List<String> idList) throws ServiceException;

    boolean deleteById(String id) throws ServiceException;

    boolean deleteByWhere(Map<String, Object> map) throws ServiceException;

    boolean logicDeleteById(String id) throws ServiceException;

    boolean batchDelete(List<String> idList) throws ServiceException;

    boolean logicBatchDelete(List<String> idList) throws ServiceException;

    Entity findById(String id) throws ServiceException;

    Entity findByName(String name) throws ServiceException;

    List<Entity> findAll() throws ServiceException;

    List<Entity> findByMap(Map<String, Object> map, String mapperFunc) throws ServiceException;
}
