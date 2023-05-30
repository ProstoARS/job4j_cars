package ru.job4j.cars.util;

import lombok.NoArgsConstructor;
import ru.job4j.cars.model.User;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.TimeZone;

@NoArgsConstructor
public class SessionUser {

    public static User getSessionUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = User.builder()
                    .login("Гость")
                    .timeZone(TimeZone.getDefault().getID())
                    .build();
        } else {
            user.setLogin(user.getLogin());
        }
        return user;
    }
}
