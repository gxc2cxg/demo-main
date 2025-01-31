package com.xuchen.demo.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xuchen.demo.common.util.UserContext;
import com.xuchen.demo.model.common.constant.ContextConstant;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader(ContextConstant.INNER_CONTEXT_NAME);
        if (StringUtils.isNotBlank(userId)) {
            UserContext.setUserId(Long.valueOf(userId));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserContext.removeUserId();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUserId();
    }
}
