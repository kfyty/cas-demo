package com.kfyty.cas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/7 15:11
 * @email kfyty725@hotmail.com
 */
@RestController
@EnableWebSecurity
@SpringBootApplication
public class ClientApplication {

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("refuse")
    public String refuse() {
        return "refuse";
    }

    @GetMapping("logoutSuccess")
    public String logoutSuccess() {
        return "logoutSuccess";
    }

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("root")
    public String root() {
        return "root";
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
