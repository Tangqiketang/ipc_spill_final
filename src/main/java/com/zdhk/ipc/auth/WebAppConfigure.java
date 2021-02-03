package com.zdhk.ipc.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2020-12-04 11:12
 */
@Configuration
public class WebAppConfigure implements WebMvcConfigurer {

    //图片上传路径
    public static final String APP_UPLOAD = "/app_upload/";

    @Value("${app.location.win}")
    private String winLocation;

    @Value("${app.location.lin}")
    private String linLocation;

    @Bean
    public TokenInterceptor getTokenInterceptor(){
        return new TokenInterceptor();
    }


    /**
     * 一个*：只匹配字符，不匹配路径（/）
     * 两个**：匹配字符，和路径（/）
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getTokenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/**",
                                     "/orderTest/**",
                                      "/camera/**",
                                      "/swagger-resources",
                                      "/exclude/**");
    }


    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // **代表所有路径
                .allowedOrigins("*") // allowOrigin指可以通过的ip，*代表所有，可以使用指定的ip，多个的话可以用逗号分隔，默认为*
                .allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE", RequestMethod.OPTIONS.name()) // 指请求方式 默认为*
                .allowCredentials(false) // 支持证书，默认为true
                .maxAge(3600) // 最大过期时间，默认为-1
                .allowedHeaders("*");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        registry.addResourceHandler(new String[]{"swagger-ui.html"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/"});
        registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"});
        if(os.startsWith("win") || os.startsWith("Win") ){
            registry.addResourceHandler(APP_UPLOAD + "**").addResourceLocations("file:///" + winLocation);
        }else{
            registry.addResourceHandler(APP_UPLOAD + "**").addResourceLocations("file://" + linLocation);
        }
    }

}
