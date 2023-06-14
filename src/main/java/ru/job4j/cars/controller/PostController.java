package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {

    private final PostService postService;
    private final EngineService engineService;
    private final CarService carService;
    private final DriverService driverService;
    private final PriceHistoryService priceHistoryService;
    private final UserService userService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        User sessionUser = SessionUser.getSessionUser(session);
        String timeZone = sessionUser.getTimeZone();
        List<Post> posts = postService.findAll(ZoneId.of(timeZone));
        model.addAttribute("user", sessionUser);
        model.addAttribute("posts", posts);
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
                             @ModelAttribute("driverName") String driverName,
                             @RequestParam("file") MultipartFile file) throws IOException {
        Optional<Engine> engine = engineService.findById(engineId);
        if (engine.isEmpty()) {
            return "redirect:/posts/formAdd";
        }
        post.getCar().setEngine(engine.get());
        Driver driver = Driver.builder().name(driverName).build();
        driverService.createDriver(driver);
        post.getCar().setOwners(Set.of(driver));
        carService.createCar(post.getCar());
        PriceHistory priceHistory = PriceHistory.builder()
                .before(0)
                .after(price)
                .created(LocalDateTime.now())
                .build();
        post.setParticipates(new ArrayList<>());
        post.getPriceHistories().add(priceHistory);
        post.setPhoto(file.getBytes());
        post.setUser(SessionUser.getSessionUser(session));
        postService.createPost(post);
        return "redirect:/posts/openPost/" + post.getId();
    }

    @GetMapping("/openPost/{postId}")
    public String openPost(Model model, HttpSession session, @PathVariable("postId") int id) {
        Post post = postService.findPostById(id).get();
        List<PriceHistory> priceHistories = priceHistoryService.findPhByPost(post);
        priceHistories.sort(Comparator.comparing(PriceHistory::getCreated));
        PriceHistory lastPrice = priceHistories.get(priceHistories.size() - 1);
        Optional<Car> car = carService.findById(post.getCar().getId());
        Set<Driver> owners = car.get().getOwners();
        User user = SessionUser.getSessionUser(session);
        Optional<User> userWithParticipates = userService.findUserWithParticipates(user);
        boolean checkParticipate = false;
        List<Post> posts = userWithParticipates.get().getPosts();
        if (!user.getLogin().equals("Гость") && !posts.isEmpty()) {
            if (posts.contains(post)) {
                checkParticipate = true;
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("price", lastPrice.getAfter());
        model.addAttribute("car", car);
        model.addAttribute("owners", owners);
        model.addAttribute("priceHistories", priceHistories);
        model.addAttribute("checkParticipate", checkParticipate);
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

    @GetMapping("/formEditPriceHistory")
    public String editPriceHistory(HttpSession session, @ModelAttribute("postId") int postId, Model model) {
        User user = SessionUser.getSessionUser(session);
        Optional<Post> postById = postService.findPostById(postId);
        if (postById.isEmpty()) {
            return "redirect:/error";
        }
        Optional<Post> withPriceHistory = postService.findPostWithPriceHistory(postById.get());
        if (withPriceHistory.isEmpty()) {
            return "redirect:/error";
        }
        Post post = withPriceHistory.get();
        List<PriceHistory> priceHistories = post.getPriceHistories();
        priceHistories.sort(Comparator.comparing(PriceHistory::getCreated));
        model.addAttribute("price", priceHistories.get(priceHistories.size() - 1).getAfter());
        model.addAttribute("priceHistories", priceHistories);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        return "/post/addNewPrice";
    }

    @PostMapping("/changePrice")
    public String updatePrice(@ModelAttribute("postId") int postId, @ModelAttribute("newPrice") long newPrice) {
        log.warn("Зашли в метод changePrice");
        Optional<Post> postById = postService.findPostById(postId);
        if (postById.isEmpty()) {
            return "redirect:/error";
        }
        Post post = postById.get();
        log.warn("нашли пост: {}", post);

        List<PriceHistory> priceHistories = priceHistoryService.findPhByPost(post);
        priceHistories.sort(Comparator.comparing(PriceHistory::getCreated));

        PriceHistory priceHistory = PriceHistory.builder()
                .post(post)
                .created(LocalDateTime.now())
                .before(priceHistories.get(priceHistories.size() - 1).getAfter())
                .after(newPrice)
                .build();
        log.warn("создали новый прайс: {}", priceHistory);

        priceHistories.add(priceHistory);
        post.setPriceHistories(priceHistories);
        postService.updatePost(post);
        return "redirect:/posts/openPost/" + postId;
    }

    @PostMapping("/delPost")
    public String deletePost(@ModelAttribute("postId") int id) {
        Optional<Post> postById = postService.findPostById(id);
        if (postById.isEmpty()) {
            return "redirect:/error";
        }
        if (!postService.deleteFromPost(postById.get())) {
            return "redirect:/error";
        }
        return "redirect:/posts/index";
    }
}
