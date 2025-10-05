package dev.tbyte.auth.config;

import dev.tbyte.auth.entity.Authority;
import dev.tbyte.auth.entity.Role;
import dev.tbyte.auth.repository.AuthorityRepository;
import dev.tbyte.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public DataInitializer(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createDefaultAuthorities();
        createDefaultRoles();
    }

    private void createDefaultAuthorities() {
        List<String> authorities = Arrays.asList("READ", "WRITE", "DELETE");
        for (String auth : authorities) {
            if (authorityRepository.findByName(auth).isEmpty()) {
                Authority authority = new Authority();
                authority.setName(auth);
                authorityRepository.save(authority);
            }
        }
    }

    private void createDefaultRoles() {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        for (String roleName : roles) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                role.setCode(roleName.replace("ROLE_", ""));
                roleRepository.save(role);
            }
        }
    }
}