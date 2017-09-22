package com.hq.CloudPlatform.ProxyServer.restful;

import com.hq.CloudPlatform.ProxyServer.exception.ServiceException;
import com.hq.CloudPlatform.ProxyServer.sys.Constants;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Created by Administrator on 7/4/2017.
 */
public interface IProxyRestService {

    @GET
    @Path("register")
    @Produces(Constants.MediaType.APPLICATION_JSON)
    String register(@QueryParam("url") String url, @QueryParam("type") String type) throws ServiceException;

}
