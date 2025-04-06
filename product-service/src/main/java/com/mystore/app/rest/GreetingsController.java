package com.mystore.app.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public class GreetingsController {


    @Value(("${server.port}"))
    String port;

    @GetMapping("/sayHi/{name}")
    public String sayHi(@PathVariable("name") String name) {
        return "Hi" + name + "Product service running at" + port;
    }
}
