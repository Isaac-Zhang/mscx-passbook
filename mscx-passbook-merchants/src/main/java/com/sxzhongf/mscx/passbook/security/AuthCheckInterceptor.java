package com.sxzhongf.mscx.passbook.security;

import com.sxzhongf.mscx.passbook.constant.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AuthCheckInterceptor for 权限拦截器
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see HandlerInterceptor
 * @since 2019/5/24
 */
@Component
public class AuthCheckInterceptor implements HandlerInterceptor {

    /**
     * controller handle处理之前优先处理
     *
     * 如果验证通过，添加token
     * @param request
     * @param response
     * @param handler
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(Constants.TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            throw new Exception("Http Header 中缺少" + Constants.TOKEN_KEY + "!");
        }
        if (!token.equals(Constants.TOKEN_VALUE)) {
            throw new Exception("Http Header " + Constants.TOKEN_KEY + " 错误!");
        }

        AccessContext.setToken(token);
        return true;
    }

    /**
     * 后处理回调方法，实现处理器的后处理（但在渲染视图之前），
     * 此时可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，
     * 类似于try-catch-finally中的finally，但仅调用处理器执行链中
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AccessContext.clearAccessKey();
    }
}
