package starbug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 박민재 스케쥴러 어노테이션
//@EnableAsync // 박민재 멀티스레드 어노테이션
@EnableJpaAuditing
@SpringBootApplication
public class StarBugStart {
    public static void main(String[] args) {
        SpringApplication.run(StarBugStart.class);
    }
}
