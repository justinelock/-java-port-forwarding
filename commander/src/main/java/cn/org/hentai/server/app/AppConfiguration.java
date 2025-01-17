package cn.org.hentai.server.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by matrixy on 2017/12/13.
 */
@Configuration
public class AppConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CommonInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/manage/**");
        super.addInterceptors(registry);
    }
}
