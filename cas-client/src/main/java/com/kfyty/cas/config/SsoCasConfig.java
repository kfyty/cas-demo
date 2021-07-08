package com.kfyty.cas.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/7 15:50
 * @email kfyty725@hotmail.com
 */
@Configuration
public class SsoCasConfig {
    @Autowired
    private CasConfigProperties casConfigProperties;

    /**
     * 设置 cas 服务相关属性
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(this.casConfigProperties.getClient().getLoginUrl());
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    /**
     * 注销单点登录
     * 由 LogoutFilter 转发到 cas 进行注销后，cas 再回调此过滤器进行本地单点登录注销
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    /**
     * ticket 校验器
     */
    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casConfigProperties.getServer().getPrefix());
    }

    /**
     * 提供 cas 校验，最终会调用到 Cas20ServiceTicketValidator
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(AuthenticationUserDetailsService<CasAssertionAuthenticationToken> userDetailsService) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setKey(CasAuthenticationProvider.class.getSimpleName());
        provider.setServiceProperties(serviceProperties());
        provider.setTicketValidator(cas20ServiceTicketValidator());
        provider.setAuthenticationUserDetailsService(userDetailsService);
        return provider;
    }
}
