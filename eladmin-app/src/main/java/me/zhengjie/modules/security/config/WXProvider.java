package me.zhengjie.modules.security.config;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.security.config.bean.WXAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Data
@Slf4j
public class WXProvider implements AuthenticationProvider {
    public UserDetailsService userDetailsService;

    /**
     * 取到authentication中的openId，根据openId查询信息，能查到信息表示登陆成功
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authentication:{}", JSON.toJSONString(authentication));
        UserDetails loginUser = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        if(loginUser == null){
            throw new RuntimeException("登陆失败");
        }
        WXAuthenticationToken wxAuthenticationToken = new WXAuthenticationToken(loginUser);
        return wxAuthenticationToken;
    }

    /**
     * 配置当前Provider对应的wxAuthenticationToken
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (WXAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
