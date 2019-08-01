package ru.aleynikov.blogcamp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

    @RequestMapping("/")
    String redirectToFeed() {
        return "redirect:/feed";
    }
}
