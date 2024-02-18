package com.marketinginapp.startup.ms.user.security.filter;

import com.marketinginapp.startup.ms.user.domain.entity.Role;
import com.marketinginapp.startup.ms.user.domain.entity.User;
import com.marketinginapp.startup.ms.user.domain.repository.UserRepository;
import com.marketinginapp.startup.ms.user.exception.ObjectNotFoundException;
import com.marketinginapp.startup.ms.user.utils.enums.EStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Slf4j
@RequiredArgsConstructor

@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository repository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authentication income {}", authentication);
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        Optional<User> user;
        try {
            user = repository.findFirstByUsernameAndStatus(username, EStatus.ACTIVE.name());
        }catch (Exception exception) {
            log.error("{}", exception.getLocalizedMessage());
            throw new ObjectNotFoundException("User not found.");
        }
        if(user.isEmpty()){
            throw new ObjectNotFoundException("User not found.");
        }
        final List<GrantedAuthority> grantedAuthorities = grantedAuthorities(user.get().getRoles().stream().toList());
        final Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);

        log.info("Authentication out come {}", auth);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }

    private List<GrantedAuthority> grantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<String> permissions = new HashSet<>();
        if(!roles.isEmpty()){
            roles.forEach(role -> {
                permissions.add(role.getName());
            });
        }
        permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));
        return grantedAuthorities;
    }
}
