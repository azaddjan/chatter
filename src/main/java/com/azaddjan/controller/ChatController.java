package com.azaddjan.controller;

import com.azaddjan.model.Question;
import com.azaddjan.service.ChatterInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class BasicChatRestController {
    private ChatterInterface chatter;
    private ChatterInterface complexChatter;

    public BasicChatRestController(ChatterInterface chatter, @Qualifier("complex") ChatterInterface complexChatter) {
        this.chatter = chatter;
        this.complexChatter = complexChatter;
    }

    @PostMapping(value = "/chat", produces = "application/json")
    public String chat(@RequestBody Question question) {
        return chatter.chat(question).answer();
    }

    @PostMapping(value = "/complex", produces = "application/json")
    public String complex(@RequestBody Question question) {
        return complexChatter.chat(question).answer();
    }
}
