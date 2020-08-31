package ba.unsa.etf.zavrsni.app.rest.controllers;

import ba.unsa.etf.zavrsni.app.auth.AuthService;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.rest.requests.LoginInput;
import ba.unsa.etf.zavrsni.app.rest.requests.SignInInput;
import ba.unsa.etf.zavrsni.app.rest.responses.LoginPayload;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest-api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("/login")
    public LoginPayload logIn(@Valid @RequestBody LoginInput loginInput){
        String token = authService.authenticate(loginInput.getUsername(), loginInput.getPassword());
        //return new SignInPayload(token, accountService.getAccountByUsername(authData.getUsername()));
        return new LoginPayload(token, accountService.getAccountByUsername(loginInput.getUsername()).getId());
    }

    @PostMapping("/register")
    public LoginPayload signIn(@Valid @RequestBody SignInInput signInInput){
        Account savedAccount = accountService.createNewAccount(signInInput);
        String token = authService.authenticate(signInInput.getUsername(), signInInput.getPassword());
        //return new SignInPayload(token, accountService.getAccountByUsername(authData.getUsername()));
        return new LoginPayload(token, savedAccount.getId());
    }
}
