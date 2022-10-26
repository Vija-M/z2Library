package vija.accenture.z2Library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Z2LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(Z2LibraryApplication.class, args);
    }
}
