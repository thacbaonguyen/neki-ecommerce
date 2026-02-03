package com.thacbao.neki.configurations;

import com.thacbao.neki.model.Role;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.RoleRepository;
import com.thacbao.neki.repositories.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class InitialDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.init.admin.email}")
    private String ADMIN_EMAIL;

    @Value("${app.init.admin.password}")
    private String ADMIN_PASSWORD;

    @Value("${app.init.admin.fullName}")
    private String ADMIN_FULL_NAME;

    @Value("${app.init.admin.phoneNumber}")
    private String ADMIN_PHONE_NUMBER;

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotExists("USER", "Người dùng");
        createRoleIfNotExists("ADMIN", "Quản trị viên");

        createAdminUserIfNotExists();
    }

    private void createRoleIfNotExists(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = Role.builder()
                    .name(name)
                    .description(description)
                    .build();
            roleRepository.save(role);
            log.info("Da tao role : {}", name);
        } else {
            log.debug("role {} da ton tai", name);
        }
    }

    private void createAdminUserIfNotExists() {
        if (userRepository.existsByEmail(ADMIN_EMAIL)) {
            log.info("User admin: {} da ton tai", ADMIN_EMAIL);
            return;
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("chua ton tai role admin"));

        User admin = User.builder()
                .email(ADMIN_EMAIL)
                .passwordHash(passwordEncoder.encode(ADMIN_PASSWORD))
                .fullName(ADMIN_FULL_NAME)
                .phone(ADMIN_PHONE_NUMBER)
                .isActive(true)
                .emailVerified(true)
                .provider("LOCAL")
                .roles(new HashSet<>(Set.of(adminRole)))
                .build();

        userRepository.save(admin);

        log.warn("admin của hệ thống đã được tạo với tt:");
        log.warn("Email    : {}", ADMIN_EMAIL);
        log.warn("Mật khẩu : {}", ADMIN_PASSWORD);
        log.warn("Đổi pass sau khi đăng nhập");
    }
}