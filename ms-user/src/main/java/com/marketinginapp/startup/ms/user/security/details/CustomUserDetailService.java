package com.marketinginapp.startup.ms.user.security.details;

import com.marketinginapp.startup.ms.user.domain.entity.User;
import com.marketinginapp.startup.ms.user.domain.repository.UserRepository;
import com.marketinginapp.startup.ms.user.exception.UnauthorizedException;
import com.marketinginapp.startup.ms.user.utils.Constant;
import com.marketinginapp.startup.ms.user.utils.enums.EStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findFirstByUsernameOrEmailAndStatus(username, username, EStatus.ACTIVE.name());
        if(user.isEmpty()){
            //log.warn("User name {} unauthorized", username);
            throw new UnauthorizedException(Constant.INFO_UNAUTHORIZED);
        }
        return new CustomUserDetail(
                user.get().getUsername(),
                user.get().getPassword(),
                user.get().getRoles()
                        .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }

    public void saveUserAttemptAuthentication(String username) {
        Optional<User> user = repository.findFirstByUsernameAndStatus(username, EStatus.ACTIVE.name());
        if(user.isPresent()) {
            int attempt = user.get().getAttempt() + 1;
            user.get().setAttempt(attempt);
            user.get().setUpdated(LocalDateTime.now());
            if(user.get().getAttempt() > 3) {
                //log.warn("User {} update status to blocked", username);
                user.get().setStatus(EStatus.CANCEL);
            }
            repository.save(user.get());
        }
    }
    public void updateAttempt(String username) {
        Optional<User> user = repository.findFirstByUsernameAndStatus(username, EStatus.ACTIVE.name());
        if(user.isPresent()) {
            user.get().setAttempt(0);
            user.get().setUpdated(LocalDateTime.now());
            repository.save(user.get());
        }
    }
}