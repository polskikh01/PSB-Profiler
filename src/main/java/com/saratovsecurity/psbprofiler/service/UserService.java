package com.saratovsecurity.psbprofiler.service;

import com.saratovsecurity.psbprofiler.entity.Role;
import com.saratovsecurity.psbprofiler.entity.UserEntity;
import com.saratovsecurity.psbprofiler.repository.UserRepository;
import com.saratovsecurity.psbprofiler.security.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(UserEntity logged) {
        UserEntity userEntity = userRepository.findByLogin(logged.getLogin()).get();
        return new AuthResponse(userEntity);
    }

    public boolean canLogin(UserEntity logged) {
        UserEntity userEntity = userRepository.findByLogin(logged.getLogin()).get();

        return userEntity != null && isPasswordMatches(logged.getPassword(), userEntity.getPassword());
    }

    private boolean isPasswordMatches(String unencodedPassword, String encodedPassword) {
        return passwordEncoder.matches(unencodedPassword, encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> currentUser = userRepository.findByLogin(username);
        if(currentUser.isPresent()) {
            return currentUser.get();
        }else{
            throw new UsernameNotFoundException("Пользователь не найден");
        }
    }
}
