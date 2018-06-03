package com.larry.expirationslackbot.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food extends BaseTimeEntity{

    @Id
    @GeneratedValue
    private Long id; // Long -> null 체크를 가능하게 함으로써 유저정보 유무를 확인

    private String name;
    private LocalDateTime expirationDate;

    @Builder
    public Food(String name, LocalDateTime expirationDate) {
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public int remainDays(LocalDateTime now) {
        return (int)now.until(expirationDate, ChronoUnit.DAYS);
    }

    // 스케쥴러 실행 시 자동으로 상태를 바꿧으면 하는데.. 근데 만약 상태를 바꿔서 Db를 계속 업데이트 하면 I/O 부하가 걸릴 수 있다. 이 정도로는 택도없지만
    public FoodStatus isStatus(LocalDateTime now) {
        int remains = remainDays(now);
        if (remains < 0) return FoodStatus.EXPIRED;
        if (remains <= 15 && (remains%5 == 0 || remains == 1 || remains == 3)) {
            return FoodStatus.WARNING;
        }
        return FoodStatus.FINE;
    }
}
