package com.hq.CloudPlatform.ProxyServer.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseMapper<Entity> {

    void save(Entity entity);

    void update(Entity entity);

    void batchUpdate(Map<String, Object> map);

    void deleteByWhere(Map<String, Object> map);

    void deleteById(String id);

    void logicDeleteById(String id);

    void batchDelete(@Param("idList") List<String> idList);

    void logicBatchDelete(@Param("idList") List<String> idList);

    Entity findById(String id);

    Entity findByName(String name);

    List<Entity> findAll();

    List<Entity> findByMap(Map<String, Object> map);

    Integer getCount(Map<String, Object> map);

    List<Entity> findByPage(Map<String, Object> map);
}
