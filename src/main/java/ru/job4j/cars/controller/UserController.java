package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.PostService;
import ru.job4j.cars.service.UserService;
import ru.job4j.cars.util.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TimeZone;

@Controller
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/formRegistration")
    public String formRegistration(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        model.addAttribute("timeZonesIDs", TimeZone.getAvailableIDs());
        model.addAttribute("defaultTZ", TimeZone.getDefault().getID());
        return "/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user) {
        Optional<User> regUser = userService.create(user);
        if (regUser.isEmpty()) {
            return "redirect:/formRegistration?fail=true";
        }
        return "redirect:/success";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findByLoginAndPassword(
                user.getLogin(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/posts/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @PostMapping("/participate")
    public String subscribe(HttpSession session, @ModelAttribute("postId") int postId) {
        Optional<Post> optionalPost = postService.findPostById(postId);
        if (optionalPost.isEmpty()) {
            log.error("Пост не вытащился с базы");
            return "redirect:/error";
        }
        Post postById = optionalPost.get();
        User sessionUser = SessionUser.getSessionUser(session);
        sessionUser.setPosts(new ArrayList<>());
        postById.setParticipates(new ArrayList<>());
        Optional<User> optionalUser = userService.findUserWithParticipates(sessionUser);
        User user = optionalUser.get();
        if (userService.addParticipate(user, postById)) {
            log.error("В коллекции не добавились данные");
            return "redirect:/error";
        }
        userService.update(user);
        return "redirect:/posts/openPost/" + postId;
    }

    @PostMapping("/delParticipate")
    public String delParticipate(HttpSession session, @ModelAttribute("postId") int postId) {
        Optional<Post> optionalPost = postService.findPostById(postId);
        if (optionalPost.isEmpty()) {
            log.error("Пост не вытащился с базы");
            return "redirect:/error";
        }
        Post post = optionalPost.get();
        User sessionUser = SessionUser.getSessionUser(session);
        sessionUser.setPosts(new ArrayList<>());
        post.setParticipates(new ArrayList<>());
        Optional<User> optionalUser = userService.findUserWithParticipates(sessionUser);
        User user = optionalUser.get();
        if (userService.delParticipate(user, post)) {
            log.error("В коллекции не удалились данные");
            return "redirect:/error";
        }
        userService.update(user);
        return "redirect:/posts/openPost/" + postId;
    }
}
