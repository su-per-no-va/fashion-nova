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
        model.addAttribute("SignupRequestDto", new SignupRequestDto());
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

    @GetMapping("/wish-list.html")
    public String wishList() {
        return "wish-list";
    }

    @GetMapping("/my-info.html")
    public String myInfo() {
        return "my-info";
    }

    @GetMapping("/grade.html")
    public String grade() {
        return "grade";
    }

    @GetMapping("/my-question.html")
    public String myQuestion() {
        return "my-question";
    }

    @GetMapping("/order.html")
    public String order() {
        return "order";
    }

    @GetMapping("/product-search.html")
    public String productSearch() {
        return "product-search";
    }

    @GetMapping("/product-search-null.html")
    public String productSearchNull() {
        return "product-search-null";
    }

    @GetMapping("/review.html")
    public String review() {
        return "review";
    }

    @GetMapping("/order-detail.html")
    public String orderDetail() {
        return "order-detail";
    }

    @GetMapping("/my-question-detail.html")
    public String myQuestionDetail() {
        return "my-question-detail";
    }

    @GetMapping("/payment.html")
    public String payment() {
        return "payment";
    }

    @GetMapping("/my-mileage.html")
    public String myMileage() {
        return "my-mileage";
    }

    @GetMapping("/my-coupon.html")
    public String myCoupon() {
        return "my-coupon";
    }

    @GetMapping("/payments-completed.html")
    public String paymentsCompleted() {
        return "payments-completed";
    }

    @GetMapping("/warn.html")
    public String warn() {
        return "warn";
    }

    // 어드민 페이지
    @GetMapping("admin/admin-coupon-mileage.html")
    public String adminCouponMileage() {
        return "admin/admin-coupon-mileage";
    }

    @GetMapping("admin/admin-login.html")
    public String adminLogin() {
        return "admin/admin-login";
    }

    @GetMapping("admin/admin-password.html")
    public String adminPassword() {
        return "admin/admin-password";
    }

    @GetMapping("admin/admin-product.html")
    public String adminProduct() {
        return "admin/admin-product";
    }

    @GetMapping("admin/admin-product-create.html")
    public String adminProductCreate() {
        return "admin/admin-product-create";
    }

    @GetMapping("admin/admin-product-update.html")
    public String adminProductUpdate() {
        return "admin/admin-product-update";
    }

    @GetMapping("admin/admin-question.html")
    public String adminQuestion() {
        return "admin/admin-question";
    }

    @GetMapping("admin/admin-review.html")
    public String adminReview() {
        return "admin/admin-review";
    }

    @GetMapping("admin/admin-signup.html")
    public String adminSignup() {
        return "admin/admin-signup";
    }

    @GetMapping("admin/admin-user.html")
    public String adminUser() {
        return "admin/admin-user";
    }

    @GetMapping("admin/index.html")
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping("kakaopay.html")
    public String kakaoPay() {
        return "kakaopay";
    }

    @GetMapping("/payments-canceled.html")
    public String paymentsCanceled() {
        return "/payments-canceled";
    }
}
