package com.supernova.fashionnova.front;

import com.supernova.fashionnova.domain.user.dto.SignupRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    //메인 페이지
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login.html")
    public String login() {
        return "login";
    }

    @GetMapping("/signup.html")
    public String signup(Model model) {
        model.addAttribute("SignupRequestDto",new SignupRequestDto());
        return "signup";
    }

    @GetMapping("/about.html")
    public String about() {
        return "about";
    }

    @GetMapping("/product.html")
    public String product() {
        return "product";
    }

    @GetMapping("/contact.html")
    public String contact() {
        return "contact";
    }

    @GetMapping("/my-page.html")
    public String myPage() {
        return "my-page";
    }

    @GetMapping("/product-detail.html")
    public String productDetail() {
        return "product-detail";
    }

    @GetMapping("/question.html")
    public String question() {
        return "question";
    }

    @GetMapping("/shoping-cart.html")
    public String shopingCart() {
        return "shoping-cart";
    }

    @GetMapping("/index.html")
    public String index() {
        return "index";
    }
}
