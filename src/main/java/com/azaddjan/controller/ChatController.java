package com.azaddjan.controller;

import com.azaddjan.model.Answer;
import com.azaddjan.model.Question;
import com.azaddjan.service.ChatterInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class ChatController {
    private ChatterInterface chatter;
    private ChatterInterface complexChatter;

    public ChatController(ChatterInterface chatter, @Qualifier("complex") ChatterInterface complexChatter) {
        this.chatter = chatter;
        this.complexChatter = complexChatter;
    }

    @GetMapping("/test")
    public Answer test() {
        return new Answer("This is a test response.");
    }


    @PostMapping(value = "/chat", produces = "application/json")
    public Answer chat(@RequestBody Question question) {
        return chatter.chat(question);
    }

    @PostMapping(value = "/complex", produces = "application/json")
    public Answer complex(@RequestBody Question question) {
        return complexChatter.chat(question);
    }
}
