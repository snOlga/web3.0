package web.ozon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.ozon.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @GetMapping("/sign/{login}/{password}")
    public ResponseEntity<String> signUp(@PathVariable String login, @PathVariable String password) {
        String token = authService.signUser(login, password);
        return (token != null || token != "") ? new ResponseEntity<>(token, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/log/{login}/{password}")
    public ResponseEntity<String> logIn(@PathVariable String login, @PathVariable String password) {
        String token = authService.logUser(login, password);
        return (token != null || token != "") ? new ResponseEntity<>(token, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
}
