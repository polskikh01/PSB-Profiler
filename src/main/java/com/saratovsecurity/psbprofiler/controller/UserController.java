package com.saratovsecurity.psbprofiler.controller;

import com.saratovsecurity.psbprofiler.entity.*;
import com.saratovsecurity.psbprofiler.repository.*;
import com.saratovsecurity.psbprofiler.security.AuthResponse;
import com.saratovsecurity.psbprofiler.service.DirService;
import com.saratovsecurity.psbprofiler.service.UserService;
import com.sun.security.auth.UserPrincipal;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DirService dirService;

    @GetMapping("/profile/{id:\\d+}")
    public ResponseEntity userPage(@AuthenticationPrincipal UserPrincipal principal, @PathVariable("id") UserEntity user) {
        if(user != null) {

            try {
                final File folder = ResourceUtils.getFile("classpath:files");
                dirService.listFilesForFolder(folder);


            }catch(Exception e){
                System.out.println("Такой папки нет");
            }

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
        System.out.println("hello");
        //BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
        return "ok";
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserEntity userEntity) {
        System.out.println(userEntity.getLogin()+userEntity.getPassword()+userEntity.getId());
        try {
            if (userService.canLogin(userEntity)) {
                AuthResponse response = userService.login(userEntity);
                return ResponseEntity.ok().body(response);
            }
        }catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Ошибка при авторизации");
        }
        return ResponseEntity.badRequest().body("Ошибка при авторизации");
    }

    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestBody String file) {
        //Optional<UserEntity> user = userRepository.findById(id);
        //JSONObject obj = new JSONObject(jsonString);
        //System.out.println();
        System.out.println(file);

        return ResponseEntity.ok().body("ok");
    }
}