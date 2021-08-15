package br.com.wishlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"br.com.wishlist"})
public class WishlistApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WishlistApplication.class, args);
    }

}
