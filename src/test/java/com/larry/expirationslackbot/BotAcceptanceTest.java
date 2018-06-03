package com.larry.expirationslackbot;

import com.larry.expirationslackbot.domain.FoodStatus;
import com.larry.expirationslackbot.domain.HtmlFormDataBuilder;
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
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static com.larry.expirationslackbot.domain.MySchedule.BOT_URL;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BotAcceptanceTest {

    private final Logger log = LoggerFactory.getLogger(BotAcceptanceTest.class);

    private HttpEntity<MultiValueMap<String, Object>> request;
    private ResponseEntity<String> response;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    public TestRestTemplate template() {
        return restTemplate;
    }

    @Test
    public void checkDate() {
        assertThat(foodRepository.findById(1L).get().isStatus(LocalDateTime.now()), is(FoodStatus.FINE));
        assertThat(foodRepository.findById(2L).get().isStatus(LocalDateTime.now()), is(FoodStatus.WARNING));
        assertThat(foodRepository.findById(3L).get().isStatus(LocalDateTime.now()), is(FoodStatus.EXPIRED));
    }

    @Test
    public void sendTestManually() {
        request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("text", "this is spring test").build();
        log.debug("request : {}", request);
        template().postForEntity(BOT_URL, request, String.class);
    }
}
