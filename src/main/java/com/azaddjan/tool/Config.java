package com.azaddjan.tool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class Config {
    @Bean
    @Description("Get the weather in location")
    public Function<WeatherService.Request, WeatherService.Response> currentWeather() {
        return new WeatherService();
    }
}
