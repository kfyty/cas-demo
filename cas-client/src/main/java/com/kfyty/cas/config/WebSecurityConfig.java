package com.kfyty.cas.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/7 16:15
 * @email kfyty725@hotmail.com
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CasConfigProperties casConfigProperties;

    @Autowired
    private ServiceProperties serviceProperties;

    @Autowired
    private SingleSignOutFilter singleSignOutFilter;

    @Autowired
    private CasAuthenticationEntryPoint casAuthenticationEntryPoint;

    @Autowired
    private CasAuthenticationProvider casAuthenticationProvider;

    @Autowired
    private PermissionDecisionManager permissionDecisionManager;

    /**
     * 授权失败：匿名用户
     * 跳转到登录页面，这里跳转到 cas
     */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setServiceProperties(serviceProperties);
        entryPoint.setLoginUrl(casConfigProperties.getServer().getLoginUrl());
        return entryPoint;
    }

    /**
     * 授权失败：已登录用户
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/refuse");
        return accessDeniedHandler;
    }

    /**
     * cas 过滤器，也是 cas 认证入口
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setServiceProperties(serviceProperties);
        casAuthenticationFilter.setFilterProcessesUrl(serviceProperties.getService());
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return casAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(casAuthenticationProvider);
    }

    /**
     * 自定义过滤规则及其安全配置
     * 对于注销登录过滤器，本地注销后转发到 cas 进行注销
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String logoutUrl = casConfigProperties.getClient().getLogoutUrl().replace(casConfigProperties.getClient().getHost(), "");
        String logoutSuccessUrl = casConfigProperties.getServer().getLogoutUrl() + "?service=" + casConfigProperties.getClient().getLogoutSuccessUrl();
        http.csrf().disable()
                .logout().logoutUrl(logoutUrl).logoutSuccessUrl(logoutSuccessUrl).permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/logoutSuccess").permitAll()
                .anyRequest().authenticated()
                .accessDecisionManager(permissionDecisionManager)
                .and()
                .addFilterAfter(singleSignOutFilter, LogoutFilter.class)
                .addFilterAfter(casAuthenticationFilter(), SingleSignOutFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(casAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler());
    }
}
