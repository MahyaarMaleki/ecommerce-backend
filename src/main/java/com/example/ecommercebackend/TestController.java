package com.example.ecommercebackend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mahyar Maleki
 */

@Controller
@RequestMapping(path = "/")
public class TestController {

    @GetMapping(path = "/hello")
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}
