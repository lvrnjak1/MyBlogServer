package ba.unsa.etf.zavrsni.app.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest-api")
public class AuthController {

    @GetMapping("/hello")
    public String getHello(){
        return "hello world";
    }
}
