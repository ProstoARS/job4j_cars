package ru.job4j.cars.util;

import lombok.NoArgsConstructor;
import ru.job4j.cars.model.User;

import javax.servlet.http.HttpSession;

@NoArgsConstructor
public class SessionUser {

    public static User getSessionUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setLogin("Гость");
        } else {
            user.setLogin(user.getLogin());
        }
        return user;
    }
}
