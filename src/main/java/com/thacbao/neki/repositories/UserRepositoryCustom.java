package com.thacbao.neki.repositories;

import com.thacbao.neki.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    Page<User> findAllActiveUsers(Pageable pageable);
    Page<User> findAllBlockedUsers(Pageable pageable);
    Page<User> findUsersByRole(String roleName, Pageable pageable);
    long countActiveVerifiedUsers();
}
