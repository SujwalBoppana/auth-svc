package dev.tbyte.auth.config;

import dev.tbyte.auth.entity.Authority;
import dev.tbyte.auth.entity.Role;
import dev.tbyte.auth.repository.AuthorityRepository;
import dev.tbyte.auth.repository.RoleRepository;
import dev.tbyte.auth.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Value("${auth.password.salt}")
    private String salt;

    public DataInitializer(RoleRepository roleRepository, AuthorityRepository authorityRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @Override
    public void run(String... args) throws Exception {
        createDefaultAuthorities();
        createDefaultRoles();
        createGuestUser();
    }

    private void createDefaultAuthorities() {
        List<String> authorities = Arrays.asList("READ", "WRITE", "DELETE");
        for (String auth : authorities) {
            if (authorityRepository.findByName(auth).isEmpty()) {
                Authority authority = new Authority();
                authority.setName(auth);
                authority.setCode(auth);
                authorityRepository.save(authority);
            }
        }
    }

    private void createDefaultRoles() {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_GUEST");
        for (String roleName : roles) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                role.setCode(roleName.replace("ROLE_", ""));
                roleRepository.save(role);
            }
        }
    }

    @Transactional
    private void createGuestUser() {
        if (userRepository.findById(3L).isEmpty()) {
            Role guestRole = roleRepository.findByCode("GUEST")
                    .orElseThrow(() -> new RuntimeException("GUEST role not found. Make sure it's created before this method is called."));

            String encodedPassword = passwordEncoder.encode("guest" + salt);

            String sql = "INSERT INTO users (id, first_name, last_name, email, password, role_id, is_deleted, created_at, updated_at, created_by, updated_by, dob, gender) " +
                    "VALUES (:id, :firstName, :lastName, :email, :password, :roleId, false, NOW(), NOW(), :createdBy, :updatedBy, NULL, NULL)";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("id", 3L);
            query.setParameter("firstName", "Guest");
            query.setParameter("lastName", "User");
            query.setParameter("email", "guest@example.com");
            query.setParameter("password", encodedPassword);
            query.setParameter("roleId", guestRole.getId());
            query.setParameter("createdBy", 3L);
            query.setParameter("updatedBy", 3L);

            query.executeUpdate();
        }
    }
}