package com.hq.CloudPlatform.ProxyServer.restful.impl;

import com.alibaba.fastjson.JSON;
import com.hq.CloudPlatform.ProxyServer.exception.ServiceException;
import com.hq.CloudPlatform.ProxyServer.restful.ISysRestService;
import com.hq.CloudPlatform.ProxyServer.restful.view.JsonViewObject;
import com.hq.CloudPlatform.ProxyServer.restful.view.User;
import com.hq.CloudPlatform.ProxyServer.sys.Constants;
import com.hq.CloudPlatform.ProxyServer.utils.SysUtils;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Path("sys")
@Component
public class SysRestServiceImpl implements ISysRestService {

    @Autowired
    @Lazy
    protected HttpServletRequest request;

    /**
     * 此方法只是用于调试登陆
     *
     * @return
     */
    @Override
    public String login(String jsonStr) {
        //模拟登陆
        User user = JSON.parseObject(jsonStr, User.class);
        HttpSession session = request.getSession();

        WebTarget client = SysUtils.getCAWebTarget()
                .path("/public/user/getByLoginName")
                .queryParam("loginName", user.getLoginName());

        String userJsonStr = client.request().get(String.class);
        JsonViewObject jsonObj = JSON.parseObject(userJsonStr, JsonViewObject.class);
        user = JSON.parseObject(jsonObj.getContentAsStr(), User.class);
        session.setAttribute(Constants.SESSION_KEY_USER, user);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), "");
        subject.login(token);
        JsonViewObject jsonView = new JsonViewObject();
        jsonView.successPack(user);

        return JSON.toJSONStringWithDateFormat(jsonView, "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String logout() throws ServiceException {
        JsonViewObject jsonView = new JsonViewObject();
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.logout();
            request.getSession().removeAttribute(Constants.SESSION_KEY_USER);
            jsonView.successPack("success");
        } catch (AuthenticationException e) {
            jsonView.failPack("用户退出异常，请重试");
        }

        return JSON.toJSONStringWithDateFormat(jsonView, "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String getCurrentUserPermissions() throws ServiceException {
        User user = (User) request.getSession().getAttribute(Constants.SESSION_KEY_USER);

        WebTarget client = SysUtils.getCAWebTarget()
                .path("/public/permission/getAllByLoginName")
                .queryParam("loginName", user.getLoginName());

        return client.request().get(String.class);
    }

    @Override
    public String getCurrentUser() throws ServiceException {
        JsonViewObject jsonView = new JsonViewObject();
        User user = (User) request.getSession().getAttribute(Constants.SESSION_KEY_USER);

        if (user != null) {
            jsonView.successPack(user);
        }

        return JSON.toJSONStringWithDateFormat(jsonView, "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String getCurrentUserPermissionsTree() throws ServiceException {
        User user = (User) request.getSession().getAttribute(Constants.SESSION_KEY_USER);

        WebTarget client = SysUtils.getCAWebTarget()
                .path("/public/permission/getAllForTreeByLoginName")
                .queryParam("loginName", user.getLoginName())
                .queryParam("appCode", Constants.APP_CODE);

        return client.request().get(String.class);
    }

    @Override
    public String testJson() throws ServiceException {
        Map<String, String> testResult = new HashMap<>();
        testResult.put("test1", "hello");
        testResult.put("test2", "world");

        return JSON.toJSONString(testResult);
    }

    @Override
    public String testParams(String param1, String param2) throws ServiceException {
        return "Result:" + param1 + " | " + param2;
    }

    @Override
    public Response testImage() throws ServiceException {
        Response.ResponseBuilder builder = Response
                .ok((StreamingOutput) output -> {
                    FileUtils.copyFile(new File("d:/test2.png"), output);
                });

        builder.header("content-type", "image/png").header("Cache-Control", "no-cache");

        return builder.build();
    }

    @Override
    public Response testFile() throws ServiceException {
        File file = new File("F:\\iso\\windows\\cn_windows_7_ultimate_with_sp1_x64_dvd_u_677408.iso");

        Response.ResponseBuilder builder = Response
                .ok((StreamingOutput) output -> {
                    //FileUtils.copyFile(new File("d:/test3.txt"), output);
                    FileUtils.copyFile(file, output);
                });

        builder.header("Content-disposition", "attachment;filename=" + "test5.iso");

        builder.header("content-type", "application/octet-stream").header("Cache-Control", "no-cache");
        builder.header("Content-Length", file.length());

        return builder.build();
    }


}
