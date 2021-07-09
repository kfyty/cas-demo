package com.kfyty.cas.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/7 17:04
 * @email kfyty725@hotmail.com
 */
@Data
@Entity
public class User {
    @Id
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String role;
    private String url;

    @Transient
    private boolean isCredentialsExpired = true;
}
