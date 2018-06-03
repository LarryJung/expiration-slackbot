package com.larry.expirationslackbot;

import com.larry.expirationslackbot.domain.FoodStatus;
import com.larry.expirationslackbot.repository.FoodRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BotAcceptanceTest {

    private final Logger log = LoggerFactory.getLogger(BotAcceptanceTest.class);

    private HttpEntity<MultiValueMap<String, Object>> request;
    private ResponseEntity<String> response;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private TestRestTemplate template;

    public TestRestTemplate template() {
        return template;
    }

    @Test
    public void checkDate() {
        assertThat(foodRepository.findById(1L).get().isStatus(LocalDateTime.now()), is(FoodStatus.FINE));
        assertThat(foodRepository.findById(2L).get().isStatus(LocalDateTime.now()), is(FoodStatus.WARNING));
        assertThat(foodRepository.findById(3L).get().isStatus(LocalDateTime.now()), is(FoodStatus.EXPIRED));
    }

    private String toStringDateTime2(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}
