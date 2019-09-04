package com.liu.spring.springoauth.config.provider;

import com.liu.spring.springoauth.config.token.User;
import com.liu.util.rediscluster.RedisConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    protected Log log = LogFactory.getLog(getClass());

    //oauth2的sessionid存到数据库为1的里面
    @Autowired
//    private RedisTemplateService1 redisTemplateService1;
    private RedisConfig redisTemplateService1;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException,ServletException {
        User userDetails = (User)authentication.getPrincipal();

//        log.info("登录用户user:" + userDetails.getName() + "login"+request.getContextPath());
        log.info("IP:" + getIpAddress(request));

//        redisTemplateService1.set(userDetails.getLoginName(), request.getSession().getId(), Long.valueOf((60 * 60 * 24 * 7)));
        redisTemplateService1.getJedisCluster().set(userDetails.getLoginName(), request.getSession().getId());
        redisTemplateService1.getJedisCluster().expire(userDetails.getLoginName(), (60 * 60 * 24 * 7));

        super.onAuthenticationSuccess(request, response, authentication);
    }

    public String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
