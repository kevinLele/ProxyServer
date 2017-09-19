package com.hq.CloudPlatform.BaseFrame.sys.filter;

import com.alibaba.fastjson.JSON;
import com.hq.CloudPlatform.BaseFrame.restful.view.JsonViewObject;
import com.hq.CloudPlatform.BaseFrame.restful.view.User;
import com.hq.CloudPlatform.BaseFrame.sys.Constants;
import com.hq.CloudPlatform.BaseFrame.utils.ConfigHelper;
import com.hq.CloudPlatform.BaseFrame.utils.SysUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/26 0026.
 */
public class LoginFilter implements Filter {

    private Pattern pattern;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String ignoreUrl = filterConfig.getInitParameter("ignoreUrl");
        pattern = Pattern.compile(ignoreUrl);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse rsp = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String username = request.getRemoteUser();
        String requestPath = request.getServletPath();
        boolean isUseCAS = Boolean.parseBoolean(ConfigHelper.getValue("CAS.isOpen"));

        //设置不缓存响应头信息，强制浏览器不缓存
        rsp.setHeader("Cache-Control", "no-cache");
        rsp.setHeader("Cache-Control", "no-store");
        rsp.setHeader("Pragma", "no-cache");
        rsp.setDateHeader("Expires", 0);

        //不需要处理的静态文件直接忽略
        if (pattern.matcher(requestPath).find()) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        User user = (User) session.getAttribute(Constants.SESSION_KEY_USER);

        //用户
        if (null == user && isUseCAS) {
            //从CA系统获取当前用户的登陆信息并存在SESSION中
            WebTarget client = SysUtils.getCAWebTarget()
                    .path("/public/user/getByLoginName")
                    .queryParam("loginName", username);
            String userJsonStr = client.request().get(String.class);
            JsonViewObject jsonObj = JSON.parseObject(userJsonStr, JsonViewObject.class);
            user = JSON.parseObject(jsonObj.getContentAsStr(), User.class);
            session.setAttribute(Constants.SESSION_KEY_USER, user);

            //通过模拟登陆操作实现shiro的登陆
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, "");
            subject.login(token);
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
