package ba.unsa.etf.zavrsni.app.rest.controllers;

import ba.unsa.etf.zavrsni.app.auth.AuthService;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.rest.requests.LoginInput;
import ba.unsa.etf.zavrsni.app.rest.requests.SignInInput;
import ba.unsa.etf.zavrsni.app.rest.responses.ApiResponse;
import ba.unsa.etf.zavrsni.app.rest.responses.LoginPayload;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest-api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@Valid @RequestBody LoginInput loginInput){
        String token = null;
        try {
            token = authService.authenticate(loginInput.getUsername(), loginInput.getPassword());
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(exception.getMessage()));
        }

        //return new SignInPayload(token, accountService.getAccountByUsername(authData.getUsername()));
        return ResponseEntity.ok()
                .body(new LoginPayload(token, accountService.getAccountByUsername(loginInput.getUsername()).getId()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInInput signInInput){
        Account savedAccount = null;
        try{
            savedAccount = accountService.createNewAccount(signInInput);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(exception.getMessage()));
        }

        String token = authService.authenticate(signInInput.getUsername(), signInInput.getPassword());
        return  ResponseEntity.ok().body( new LoginPayload(token, savedAccount.getId()));
    }
}
