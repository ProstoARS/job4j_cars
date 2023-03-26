package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.PostService;
import ru.job4j.cars.util.SessionUser;

import javax.servlet.http.HttpSession;
import java.time.ZoneId;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        User sessionUser = SessionUser.getSessionUser(session);
        String timeZone = sessionUser.getTimeZone();
        model.addAttribute("user", sessionUser);
        model.addAttribute("posts", postService.findAll(ZoneId.of(timeZone)));
        return "post/index";
    }
}
