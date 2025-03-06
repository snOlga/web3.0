package web.ozon.controller;

import web.ozon.DTO.AuthUserDTO;
import web.ozon.DTO.TokenDTO;
import web.ozon.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PermitAll
    @PostMapping("/sign")
    public ResponseEntity<TokenDTO> signUp(@RequestBody AuthUserDTO dto) {
        String token = authService.signUser(dto.getLogin(), dto.getPassword());
        return (token != null || token != "") ? new ResponseEntity<>(new TokenDTO(token), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PermitAll
    @PostMapping("/log")
    public ResponseEntity<TokenDTO> logIn(@RequestBody AuthUserDTO dto) {
        String token = authService.logUser(dto.getLogin(), dto.getPassword());
        return (token != null || token != "") ? new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
