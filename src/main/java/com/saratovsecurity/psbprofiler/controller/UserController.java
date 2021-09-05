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
import java.io.FileNotFoundException;
import java.util.*;

@RestController
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DirService dirService;

    @PostMapping("/startProcessing")
    public void startProcessing(@RequestBody String command) {
        try {
            //инициализируем CV
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(ResourceUtils.getFile("classpath:tessdata").getPath());
            tesseract.setLanguage("rus");

            //получаем список файлов
            final File folder = ResourceUtils.getFile("classpath:files/Необработанные документы");
            List<String> nonProcessFiles = dirService.listFilesForFolder(folder);

            for(String currentStrFile : nonProcessFiles){
                File currentFile = ResourceUtils.getFile("classpath:files/Необработанные документы/"+currentStrFile);
                boolean innBool = false;
                boolean nameBool = false;

                //проверка на расширение документа
                String ext = currentStrFile.substring(currentStrFile.lastIndexOf("."), currentStrFile.length());

                if(ext.equals(".pdf") || ext.equals(".png") || ext.equals(".jpg") || ext.equals(".jpeg")){
                    String text = tesseract.doOCR(currentFile).toLowerCase();
                    String inn = text.substring(text.indexOf("инн")+3,text.indexOf("инн")+15).replaceAll("\\D+","");
                    if(inn.length() <= 12 && inn.length() >= 10){
                        System.out.println(inn);
                        innBool = true;
                    }

                    String name = text.substring(text.indexOf("пао"),text.indexOf("пао")+15);
                    if(text.indexOf("пао") != -1){
                        System.out.println(name);
                        nameBool = true;
                    }

                    //ДОБАВИТЬ СВЕРКУ ПО БД

                    if(innBool && nameBool){
                        //проверка маркеров на номенклатуру
                    }else{
                        //перемещаем в невалид
                        DirService.moveFile(ResourceUtils.getFile("classpath:files/Необработанные документы/"+currentStrFile).getPath(),ResourceUtils.getFile("classpath:files/Невалидные документы/").getPath()+"/"+currentStrFile);
                    }
                }else{
                    //конвертация
                    //а пока
                    continue;
                }
            }
        }catch (Exception e){
            System.out.println("Ошибка при обработке данных CV:\n");
            e.printStackTrace();
        }
    }

    @GetMapping("/profile/{id:\\d+}")
    public String userPage(@AuthenticationPrincipal UserPrincipal principal, @PathVariable("id") UserEntity user) {
        return "ok";
    }

    @GetMapping("/nonProcessed")
    public List<String> nonProcessed() throws FileNotFoundException {
        final File folder1 = ResourceUtils.getFile("classpath:files/Необработанные документы");
        List<String> toFront = dirService.listFilesForFolder(folder1);
        return toFront;
    }

    @GetMapping("/processed")
    public List<String> processed() throws FileNotFoundException {
        final File folder2 = ResourceUtils.getFile("classpath:files/Обработанные документы");
        List<String> toFront = dirService.listFilesForFolder(folder2);
        return toFront;
    }

    @GetMapping("/nonValid")
    public List<String> nonValid() throws FileNotFoundException {
        final File folder3 = ResourceUtils.getFile("classpath:files/Невалидные документы");
        List<String> toFront = dirService.listFilesForFolder(folder3);
        return toFront;
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
}