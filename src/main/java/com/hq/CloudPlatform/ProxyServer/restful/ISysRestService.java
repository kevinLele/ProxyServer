package com.hq.CloudPlatform.ProxyServer.restful;

import com.hq.CloudPlatform.ProxyServer.exception.ServiceException;
import com.hq.CloudPlatform.ProxyServer.sys.Constants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 7/4/2017.
 */
public interface ISysRestService {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String login(String jsonStr) throws ServiceException;

    @GET
    @Path("logout")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String logout() throws ServiceException;

    @GET
    @Path("getCurrentUserPermissions")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String getCurrentUserPermissions() throws ServiceException;

    @GET
    @Path("getCurrentUser")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String getCurrentUser() throws ServiceException;

    @GET
    @Path("getCurrentUserPermissionsTree")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String getCurrentUserPermissionsTree() throws ServiceException;

    @GET
    @Path("testJson")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String testJson() throws ServiceException;

    @GET
    @Path("testImage")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response testImage() throws ServiceException;

    @GET
    @Path("testFile")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response testFile() throws ServiceException;
}
