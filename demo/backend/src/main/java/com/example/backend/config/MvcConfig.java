package com.example.backend.config;

import com.example.backend.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Locale;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    //    private String locationPath = "D:\\Upload\\"; // 3.文件本地路径 /root
    private String locationPath = "file/"; // 3.文件本地路径
    private static final String netPath = "/**"; // 映射路径


    /**
     * 添加jwt拦截器，并指定拦截路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/email")
                .excludePathPatterns("/user/forget");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**") //过滤策略
//                .addResourceLocations("classpath:/static/");  // 静态资源路径

        registry.addResourceHandler("/user/avatar/**")
                .addResourceLocations("file:" +
                                System.getProperty("file.separator") +
                                "file" +
                                System.getProperty("file.separator") +
                                "user" +
                                System.getProperty("file.separator") +
                                "avatar" +
                                System.getProperty("file.separator")
                );
        registry.addResourceHandler("/user/**")
                .addResourceLocations("file:" +
                        System.getProperty("file.separator") +
                        "file" +
                        System.getProperty("file.separator") +
                        "user" +
                        System.getProperty("file.separator")
                );

        registry.addResourceHandler("/activity/illus/**").
                addResourceLocations("file:" +
//                        System.getProperty("user.dir")+
                                System.getProperty("file.separator") +
                                "file" +
                                System.getProperty("file.separator") +
                                "activity" +
                                System.getProperty("file.separator") +
                                "illus" +
                                System.getProperty("file.separator")
                );

        registry.addResourceHandler("/report/illus/**").
                addResourceLocations("file:" +
                                System.getProperty("file.separator") +
                                "file" +
                                System.getProperty("file.separator") +
                                "report" +
                                System.getProperty("file.separator") +
                                "illus" +
                                System.getProperty("file.separator")
                );
//        registry.addResourceHandler("/report/illus/**").
//                addResourceLocations("file:" +"D:/Upload/report/illus"
//                );




    }


    /**
     * jwt拦截器
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}