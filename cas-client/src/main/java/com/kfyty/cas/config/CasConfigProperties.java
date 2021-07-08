package com.kfyty.cas.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/7 15:50
 * @email kfyty725@hotmail.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cas")
public class CasConfigProperties {
    @NestedConfigurationProperty
    private Client client;

    @NestedConfigurationProperty
    private Server server;

    @Data
    public static class Base {
        protected String host;
        protected String loginUrl;
        protected String logoutUrl;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Client extends Base {
        private String loginSuccessUrl;
        private String logoutSuccessUrl;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Server extends Base {
        private String prefix;
    }
}
