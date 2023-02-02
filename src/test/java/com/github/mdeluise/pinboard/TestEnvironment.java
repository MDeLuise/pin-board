package com.github.mdeluise.pinboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.mdeluise.pinboard.authentication.User;
import com.github.mdeluise.pinboard.authorization.permission.Permission;
import com.github.mdeluise.pinboard.authorization.role.ERole;
import com.github.mdeluise.pinboard.authorization.role.Role;
import com.github.mdeluise.pinboard.security.services.UserDetailsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class TestEnvironment {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Set<Role> emptyRole = new HashSet<>();
        Set<Role> adminRole = new HashSet<>();
        adminRole.add(new Role(ERole.ROLE_ADMIN));
        Set<Permission> emptyPermission = new HashSet<>();
        User adminUser = new User("admin", passwordEncoder.encode("admin"), adminRole, emptyPermission);
        User simpleUser = new User("user", passwordEncoder.encode("user"), emptyRole, emptyPermission);
        User simpleUser1 = new User("user1", passwordEncoder.encode("user1"), emptyRole, emptyPermission);
        User simpleUser2 = new User("user2", passwordEncoder.encode("user2"), emptyRole, emptyPermission);
        Collection<User> users = Set.of(adminUser, simpleUser, simpleUser1, simpleUser2);

        return new InMemoryUserDetailsManager() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        return UserDetailsImpl.build(user);
                    }
                }
                throw new UsernameNotFoundException(username);
            }
        };
    }


    @Bean
    public ObjectMapper objectMapper() {
        // not only return new ObjectMapper() because if so, since in the test only some classes
        // are @Import, it will lead to error with Instant conversion in ModelMapper
        return JsonMapper.builder().addModule(new ParameterNamesModule()).addModule(new Jdk8Module())
                         .addModule(new JavaTimeModule()).build();

    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
