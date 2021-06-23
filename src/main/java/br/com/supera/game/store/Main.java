package br.com.supera.game.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Connect.connect("products");
        Connect.connect("cart");
        Connect.createProductTable();
        Connect.createCartTable();
        SpringApplication.run(Main.class, args);
    }

}
