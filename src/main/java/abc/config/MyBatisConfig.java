package abc.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//配置MyBatis的分页插件
//这是分页拦截器
@Configuration
public class MyBatisConfig {


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){





        MybatisPlusInterceptor mybatisPlusInterceptor=new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor=new PaginationInnerInterceptor();
        //最后一页的下一页是首页
        paginationInnerInterceptor.setOverflow(true);
        //设置单页最大限制
        paginationInnerInterceptor.setMaxLimit(500L);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);

        return mybatisPlusInterceptor;



    }


}
