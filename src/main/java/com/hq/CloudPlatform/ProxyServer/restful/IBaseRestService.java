package com.hq.CloudPlatform.ProxyServer.restful;

import com.hq.CloudPlatform.ProxyServer.sys.Constants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface IBaseRestService {

    /**
     * 分页查询
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("getPage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String getPage(String jsonStr);

    /**
     * 获取所有数据
     *
     * @return
     */
    @GET
    @Path("getAll")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String getAll();

    /**
     * 根据条件查询
     * 将条件实体bean转化成jsonStr
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("getByWhere")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String getByWhere(String jsonStr);

    /**
     * 根据id查询
     * 只返回查询到的第一条记录
     *
     * @param id
     * @return
     */
    @GET
    @Path("getById")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String getById(@QueryParam("id") String id);

    /**
     * 根据名称查询
     * 只返回查询到的第一条记录
     *
     * @param name
     * @return
     */
    @GET
    @Path("getByName")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String getByName(@QueryParam("name") String name);

    /**
     * 检查是否存在
     * 通过传入的参数传换为实体并作为条件进行查询，如果查找到则返回true,否则返回false
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("isExist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String isExist(String jsonStr);

    /**
     * 根据id进行逻辑删除
     * 逻辑删除,数据库中标识为已删除的状态，表中必须有is_delete字段
     *
     * @param id
     * @return
     */
    @GET
    @Path("removeById")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String removeById(@QueryParam("id") String id);

    /**
     * 根据传入的id列表进行批量逻辑删除
     * 逻辑删除,数据库中标识为已删除的状态，表中必须有is_delete字段
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("batchRemove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String batchRemove(String jsonStr);

    /**
     * 根据id从数据库中删除
     * 物理删除
     *
     * @param id
     * @return
     */
    @GET
    @Path("removeFromDbById")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String removeFromDbById(@QueryParam("id") String id);

    /**
     * 根据传入的id列表进行批量删除
     * 物理删除
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("batchRemoveFromDb")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String batchRemoveFromDb(String jsonStr);

    /**
     * 根据传入的条件进行删除
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("removeByWhere")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String removeByWhere(String jsonStr);

    /**
     * 保存
     * 将实体bean转化成jsonStr
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String save(String jsonStr);

    /**
     * 编辑
     * 将实体bean转化成jsonStr
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("modify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String modify(String jsonStr);

    /**
     * 批量修改
     *
     * @param jsonStr
     * @return
     */
    @POST
    @Path("batchModify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String batchModify(String jsonStr);
}
