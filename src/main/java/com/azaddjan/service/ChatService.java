package com.azaddjan.service;

import com.azaddjan.model.Answer;
import com.azaddjan.model.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ChatService implements ChatterInterface {
    @Value("classpath:prompts/simple_system_prompt.txt")
    Resource simple_system_prompt;
    private final ChatClient chatClient;


    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Answer chat(Question prompt) {

        return chatClient.prompt()
                .user(prompt.question())
                .call()
                .entity(Answer.class);
    }

}

