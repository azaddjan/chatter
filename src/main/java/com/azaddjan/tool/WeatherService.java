package com.azaddjan.tool;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

public class WeatherService  implements Function<WeatherService.Request, WeatherService.Response> {
    private static final String BASE_URL = "https://wttr.in/";
    private final RestTemplate restTemplate = new RestTemplate();

    public record Request(String location) {}
    public record Response(String temp) {}

    public Response apply(Request request) {
        String url = BASE_URL + request.location() + "?format=3&u";
        return new Response(restTemplate.getForObject(url, String.class));
    }
}

