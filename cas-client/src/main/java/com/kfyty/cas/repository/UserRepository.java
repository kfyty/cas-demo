package com.kfyty.cas.repository;

import com.kfyty.cas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/7 17:05
 * @email kfyty725@hotmail.com
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
