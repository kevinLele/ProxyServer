package com.hq.CloudPlatform.BaseFrame.service.impl;

import com.hq.CloudPlatform.BaseFrame.entity.BaseEntity;
import com.hq.CloudPlatform.BaseFrame.exception.ServiceException;
import com.hq.CloudPlatform.BaseFrame.mapper.BaseMapper;
import com.hq.CloudPlatform.BaseFrame.restful.view.Page;
import com.hq.CloudPlatform.BaseFrame.service.IBaseService;
import com.hq.CloudPlatform.BaseFrame.utils.BeanObjectToMap;
import com.hq.CloudPlatform.BaseFrame.utils.IDGenerator;
import com.hq.CloudPlatform.BaseFrame.utils.SysReflectionUtils;
import com.hq.CloudPlatform.BaseFrame.utils.validation.annotation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<Entity extends BaseEntity> implements IBaseService<Entity> {

    public abstract BaseMapper<Entity> getBaseMapper();

    /**
     * 分页查询
     */
    @Override
    public Page<Entity> findByPage(Page<Entity> page, String countMapperFunc, String pageMapperFunc) throws ServiceException {
        if (page.getStartRowNum() >= page.getEndRowNum()) {
            throw new ServiceException("分页查询时开始行必须小于结束行");
        }

        //单页查询不允许超过500条，防止恶意调用page size过大导致内存溢出
        if (page.getPageSize() > Page.MAX_PAGE_SIZE) {
            throw new ServiceException("超过允许查询的单页记录最大值");
        }

        try {
            Map<String, Object> queryParams = new HashMap<>();
            Map<String, Object> conditions = page.getConditions();

            if (null != conditions) {
                queryParams.putAll(conditions);
            }

            Integer count = SysReflectionUtils.invokeMethodByName(
                    this.getBaseMapper(),
                    countMapperFunc,
                    Integer.class,
                    new Class[]{Map.class},
                    queryParams
            );

            page.setTotal(count);
            queryParams.put("startRowNum", page.getStartRowNum());
            queryParams.put("endRowNum", page.getEndRowNum());
            queryParams.put("pageSize", page.getPageSize());
            queryParams.put("orderFields", page.getOrderFields());

            List rows = SysReflectionUtils.invokeMethodByName(
                    this.getBaseMapper(),
                    pageMapperFunc,
                    List.class,
                    new Class[]{Map.class},
                    queryParams
            );

            page.setRows(rows);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return page;
    }

    /**
     * 实现新增
     */
    @Transactional(rollbackFor = {ServiceException.class})
    @ValidationMethod
    @Override
    public String save(Entity entity) throws ServiceException {
        String id = IDGenerator.getID();

        try {
            if (StringUtils.isBlank(entity.getId()) || entity.getId().equalsIgnoreCase("0")) {
                entity.setId(id);
            }

            entity.setCreateDate(new Date());
            this.getBaseMapper().save(entity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return id;
    }

    /**
     * 修改信息
     */
    @Transactional(rollbackFor = {ServiceException.class})
    @ValidationMethod
    @Override
    public boolean update(Entity entity) throws ServiceException {
        try {
            entity.setUpdateDate(new Date());

            if (entity.getId() == null) {
                return false;
            }

            this.getBaseMapper().update(entity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return true;
    }

    @Transactional(rollbackFor = {ServiceException.class})
    @ValidationMethod
    @Override
    public boolean batchUpdate(Entity entity, List<String> idList) throws ServiceException {
        try {
            entity.setUpdateDate(new Date());

            if (entity == null || idList == null || idList.size() == 0) {
                return false;
            }

            Map<String, Object> params = BeanObjectToMap.convertBean(entity);
            params.put("idList", idList);

            this.getBaseMapper().batchUpdate(params);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return true;
    }

    /**
     * 通过条件删除
     *
     * @param map
     * @return
     * @throws ServiceException
     */
    @Transactional(rollbackFor = {ServiceException.class})
    @Override
    public boolean deleteByWhere(Map<String, Object> map) throws ServiceException {
        try {
            this.getBaseMapper().deleteByWhere(map);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return true;
    }

    /**
     * 通过id删除
     */
    @Transactional(rollbackFor = {ServiceException.class})
    @Override
    public boolean deleteById(String id) throws ServiceException {
        try {
            this.getBaseMapper().deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return true;
    }

    @Transactional(rollbackFor = {ServiceException.class})
    @Override
    public boolean logicDeleteById(String id) throws ServiceException {
        try {
            this.getBaseMapper().logicDeleteById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return true;
    }

    /**
     * 批量删除
     */
    @Transactional(rollbackFor = {ServiceException.class})
    @Override
    public boolean batchDelete(List<String> idList) throws ServiceException {
        try {
            this.getBaseMapper().batchDelete(idList);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return true;
    }

    @Transactional(rollbackFor = {ServiceException.class})
    @Override
    public boolean logicBatchDelete(List<String> idList) throws ServiceException {
        try {
            this.getBaseMapper().logicBatchDelete(idList);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return true;
    }

    /**
     * 通过id查询
     */
    @Override
    public Entity findById(String id) throws ServiceException {
        Entity entity;

        try {
            entity = this.getBaseMapper().findById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return entity;
    }

    @Override
    public Entity findByName(String name) throws ServiceException {
        Entity entity;

        try {
            entity = this.getBaseMapper().findByName(name);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return entity;
    }

    /**
     * 查询所有信息
     */
    @Override
    public List<Entity> findAll() throws ServiceException {
        List<Entity> list;

        try {
            list = this.getBaseMapper().findAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return list;
    }

    /**
     * 根据不同的条件查询
     */
    @Override
    public List<Entity> findByMap(Map<String, Object> map, String mapperFunc) throws ServiceException {
        List<Entity> list;

        try {
            list = SysReflectionUtils.invokeMethodByName(
                    this.getBaseMapper(),
                    mapperFunc,
                    List.class,
                    new Class[]{Map.class},
                    map
            );
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return list;
    }
}
