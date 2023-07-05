package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.PostService;
import ru.job4j.cars.service.UserService;
import ru.job4j.cars.util.SessionUser;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
public class ParticipateController {

    UserService userService;

    PostService postService;

    @PostMapping("/participate")
    public String subscribe(HttpSession session, @ModelAttribute("postId") int postId) {
        Post postById = getPost(postId);
        if (postById == null) {
            return "redirect:/error";
        }
        User user = getUser(session, postById);
        if (user == null) {
            return "redirect:/error";
        }
        userService.addParticipate(user, postById);
        userService.update(user);
        return "redirect:/posts/openPost/" + postId;
    }

    @PostMapping("/delParticipate")
    public String delParticipate(HttpSession session, @ModelAttribute("postId") int postId) {
        Post post = getPost(postId);
        if (post == null) {
            return "redirect:/error";
        }
        User user = getUser(session, post);
        if (user == null) {
            return "redirect:/error";
        }
        userService.delParticipate(user, post);
        userService.update(user);
        return "redirect:/posts/openPost/" + postId;
    }

    private User getUser(HttpSession session, Post post) {
        User sessionUser = SessionUser.getSessionUser(session);
        post.setParticipates(new HashSet<>());
        Optional<User> optionalUser = userService.findUserWithParticipates(sessionUser);
        if (optionalUser.isEmpty()) {
            log.error("Пользователь не вытащился с базы");
            return null;
        }
        return optionalUser.get();
    }

    private Post getPost(int postId) {
        Optional<Post> optionalPost = postService.findPostById(postId);
        if (optionalPost.isEmpty()) {
            log.error("Пост не вытащился с базы");
            return null;
        }
        return optionalPost.get();
    }
}
