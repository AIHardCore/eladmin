package me.zhengjie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author HardCore
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sys")
public class SystemProperties {

    private Config config;

    @Data
    public static class Config{
        private String name;
        private Pwd pwd;

        @Data
        public static class Pwd{

            /**
             * 用户默认密码
             */
            private String val;
            /**
             * 用户默认密码
             */
            private String key;
        }
    }
}
