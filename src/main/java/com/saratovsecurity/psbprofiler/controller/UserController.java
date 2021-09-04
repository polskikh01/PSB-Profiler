package com.saratovsecurity.psbprofiler.controller;

import com.saratovsecurity.psbprofiler.entity.*;
import com.saratovsecurity.psbprofiler.repository.*;
import com.saratovsecurity.psbprofiler.security.AuthResponse;
import com.saratovsecurity.psbprofiler.service.UserService;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile/{id:\\d+}")
    public ResponseEntity userPage(@AuthenticationPrincipal UserPrincipal principal, @PathVariable("id") UserEntity user) {
        if(user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Данного пользователя не найдено");
    }

    @GetMapping("/getAllUsers")
    public List<UserEntity> getAllUsers(@AuthenticationPrincipal UserPrincipal principal) {
        return userRepository.findAll();
    }

    @GetMapping("/login")
    public String login() {
        return "ok";
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserEntity userEntity) {
        if(userService.canLogin(userEntity)) {
            AuthResponse response = userService.login(userEntity);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body("Ошибка при авторизации");
    }

    @PostMapping("/uploadFile/{id}")
    public ResponseEntity uploadFile(@RequestBody String photoUrl, @PathVariable Long id) {
        Optional<UserEntity> user = userRepository.findById(id);

        if(user.isPresent()){
            UserEntity userPreset = user.get();
            userPreset.setPhoto(photoUrl);
            userRepository.save(userPreset);
        }

        return ResponseEntity.ok().body("hi");
    }
}