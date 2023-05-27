package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.*;
import ru.job4j.cars.util.SessionUser;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final EngineService engineService;
    private final CarService carService;
    private final DriverService driverService;
    private final PriceHistoryService priceHistoryService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        User sessionUser = SessionUser.getSessionUser(session);
        String timeZone = sessionUser.getTimeZone();
        model.addAttribute("user", sessionUser);
        model.addAttribute("posts", postService.findAll(ZoneId.of(timeZone)));
        return "post/index";
    }

    @GetMapping("/formAdd")
    public String addPost(Model model, HttpSession session) {
        model.addAttribute("user", SessionUser.getSessionUser(session));
        model.addAttribute("post", new Post());
        model.addAttribute("engines", engineService.findAll());
    return "post/add";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post, HttpSession session,
                             @ModelAttribute("engineId") int engineId,
                             @ModelAttribute("price") long price,
                             @ModelAttribute("driver") String driverName,
                             @RequestParam("file") MultipartFile file) throws IOException {
        Optional<Engine> engine = engineService.findById(engineId);
        if (engine.isEmpty()) {
            return "redirect:/formAddPost";
        }
        post.getCar().setEngine(engine.get());
        Driver driver = Driver.builder().name(driverName).build();
        driverService.createDriver(driver);
        post.getCar().setOwners(Set.of(driver));
        carService.createCar(post.getCar());
        PriceHistory priceHistory = PriceHistory.builder()
                .before(0)
                .after(price)
                .build();
        priceHistoryService.createPriceHistory(priceHistory);
        post.setPriceHistories(List.of(priceHistory));
        post.setPhoto(file.getBytes());
        post.setUser(SessionUser.getSessionUser(session));
        postService.createPost(post);
        return "redirect: /openPost" + post.getId();
    }

    @GetMapping("/openPost/{postId}")
    public String openPost(Model model, HttpSession session, @PathVariable("postId") int id) {
        Post post = postService.findPostById(id).get();
        List<PriceHistory> priceHistories = post.getPriceHistories();
        priceHistories.sort(Comparator.comparing(PriceHistory::getCreated));
        PriceHistory lastPrice = priceHistories.get(priceHistories.size() - 1);
        model.addAttribute("user", SessionUser.getSessionUser(session));
        model.addAttribute("post", post);
        model.addAttribute("price", lastPrice.getAfter());
        model.addAttribute("car", post.getCar());
        model.addAttribute("priceHistories", priceHistories);
        return "post/postInfo";
    }

    @GetMapping("/photoCar/{postId}")
    public ResponseEntity<Resource> downloadCarPhoto(@PathVariable("postId") int postId) {
        Post post = postService.findPostById(postId).get();
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(post.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(post.getPhoto()));
    }

    @GetMapping("/participate/{postId}")
    public String subscribe(@ModelAttribute User user, @PathVariable("postId") int postId) {
        user.getPosts().add(postService.findPostById(postId).get());
        return "redirect:/post/postInfo";
    }
}
