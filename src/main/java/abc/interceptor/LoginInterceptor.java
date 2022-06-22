package abc.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录检查，目标方法执行前检查
 * 1. 编写拦截器，拦截什么请求
 * 2. 把拦截器注册在放在容器里
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    //方法执前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录逻辑检查
        HttpSession session = request.getSession();
        Object manager = session.getAttribute("manager");
        Object user = session.getAttribute("user");
        if (manager != null || user!=null) {
            return true;
        } else {
            log.info("被拦截的请求路径是：{}", request.getRequestURL());
            //拦截住了就是未登录，跳转登录页面
            //因为是重定向，session生命周期已经结束了,无法带信息转发
//            response.sendRedirect("/");//重定向到首页，因为项目里设置了/,/login都是首页
//            session.setAttribute("msg", "请先登录");
            request.setAttribute("loginMsg","请先登录在访问网址");
            request.getRequestDispatcher("/").forward(request,response);
            return false;
        }
//        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
