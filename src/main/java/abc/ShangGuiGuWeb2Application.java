package abc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShangGuiGuWeb2Application {

    public static void main(String[] args) {
        SpringApplication.run(ShangGuiGuWeb2Application.class, args);
        for(String s:args){
            System.out.println(s);
        }
    }
}
