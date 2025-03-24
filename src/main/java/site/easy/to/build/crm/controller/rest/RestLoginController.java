package site.easy.to.build.crm.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.user.UserService;

@RestController
public class RestLoginController {
    @Autowired
    UserService userService;

    @GetMapping("/login/connection")
    public ResponseEntity<?> getUser(@RequestParam String email,@RequestParam String nomUser){
        User user = userService.findByEmail(email);
        if (user == null || user.getUsername().compareTo(nomUser)!=0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'email n'exist pas dans la base");
        }

        return ResponseEntity.ok(user);
    }
}
