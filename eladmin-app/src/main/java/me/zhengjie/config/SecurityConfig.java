package me.zhengjie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 放行所有的/article路径下的请求
                .authorizeRequests()
                .antMatchers("**/article/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
                .and()
        // 其他配置...
        ;
    }
}
