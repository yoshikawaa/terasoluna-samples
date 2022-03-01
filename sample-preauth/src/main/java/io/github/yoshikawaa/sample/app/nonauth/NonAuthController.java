package io.github.yoshikawaa.sample.app.nonauth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nonauth")
public class NonAuthController {

    public static final String COOKIE_NAME = "id";

    @GetMapping
    public String home(@CookieValue(name = COOKIE_NAME, required = false) String id, HttpServletResponse response) {

        // delete cookie if exists.
        if (id != null) {
            Cookie cookie = new Cookie(COOKIE_NAME, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "nonauth/home";
    }

    @PostMapping(params = "valid")
    public String valid(HttpServletResponse response) {

        // add valid cookie
        Cookie cookie = new Cookie(COOKIE_NAME, "valid-user");
        cookie.setMaxAge(10 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @PostMapping(params = "invalid")
    public String invalid(HttpServletResponse response) {

        // add invalid cookie
        Cookie cookie = new Cookie(COOKIE_NAME, "invalid-user");
        cookie.setMaxAge(10 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }
}
