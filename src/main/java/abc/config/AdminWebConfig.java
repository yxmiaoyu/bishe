package abc.config;


import abc.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //在拦截器中心里添加自己写的拦截器
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")//拦截什么请求
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**","/lib/**","/app/login");//放行那些

//        WebMvcConfigurer.super.addInterceptors(registry)/;
    }
}
