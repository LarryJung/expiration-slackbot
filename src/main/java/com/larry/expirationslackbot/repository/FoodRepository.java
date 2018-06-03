package com.larry.expirationslackbot.repository;

import com.larry.expirationslackbot.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long>{

}
