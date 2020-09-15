package com.moon.security.repository;

import com.moon.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<T extends User>  extends JpaRepository<User, Integer> {
    /**
     * 根据用户名获取用户详情信息
     * @param userName
     * @return
     */
    User findByUserName(String userName);
}
