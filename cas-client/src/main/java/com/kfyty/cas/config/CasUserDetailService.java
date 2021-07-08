package com.kfyty.cas.config;

import com.kfyty.cas.entity.User;
import com.kfyty.cas.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/7 16:08
 * @email kfyty725@hotmail.com
 */
@Slf4j
@Component
public class CasUserDetailService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        log.info("login success: {}", token.getName());
        User user = this.userRepository.findByUsername(token.getName());
        CasUserDetail casUserDetail = new CasUserDetail(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(casUserDetail, null, casUserDetail.getAuthorities()));
        return casUserDetail;
    }
}
