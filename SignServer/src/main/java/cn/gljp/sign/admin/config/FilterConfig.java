package cn.gljp.sign.admin.config;

import cn.gljp.sign.admin.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@CrossOrigin
public class FilterConfig implements WebMvcConfigurer {

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtFilter).addPathPatterns("/**")  //此路径会被拦截器处理
                .excludePathPatterns("/**/dologin").excludePathPatterns("/**/getInfo").excludePathPatterns("/**/getSchoolStructureLength")
                .excludePathPatterns("/**/getCurrentPici").excludePathPatterns("/**/authRule/error").excludePathPatterns("/index/**/")
                .excludePathPatterns("/**/login/error").excludePathPatterns("/**/sso").excludePathPatterns("/**/uploadImage")
                .excludePathPatterns("/Upload/**/").excludePathPatterns("/excel/**/").excludePathPatterns("/notice/**/").excludePathPatterns("/import/**")
                .excludePathPatterns("/admin/update/**").excludePathPatterns("/admin/upload/**").excludePathPatterns("/image/**").excludePathPatterns("/admin/schoolStructure/addSelectUnit")

        ;
    }
}
