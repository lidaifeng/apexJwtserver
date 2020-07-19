package com.apex.jwtserver.authorization.interceptor;

import com.apex.jwtserver.authorization.annotation.Authorization;
import com.apex.jwtserver.authorization.model.TokenModel;
import com.apex.jwtserver.manager.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Description: 自定义拦截器，判断此请求是否有权限
 * @Date: Created in 2020/07/18
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    /**
     * 存放Authorization的header字段
     */
    public static final String AUTHORIZATION = "authorization";

    /**
     * 存储当前登录用户id的字段名
     */
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";

    @Autowired
    private TokenManager manager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 从header中得到token
        String authorization = request.getHeader(AUTHORIZATION);

        // 验证token
        TokenModel model = this.manager.getToken(authorization);

        if (method.getAnnotation(Authorization.class) != null) {
            if (null == model) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            if (this.manager.checkToken(model)) {
                //  如果token验证成功，将token对应的userId存在request中，便于之后注入
                request.setAttribute(CURRENT_USER_ID, model.getUserId());
                return true;
            }
            //  如果验证token失败，并且方法注明了Authorization， 返回401错误
            if (method.getAnnotation(Authorization.class) != null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }
}