package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"application", "api"})
public class QuizApiApplication {
    static void main(String[] args) {
        SpringApplication.run(QuizApiApplication.class, args);
    }
}
