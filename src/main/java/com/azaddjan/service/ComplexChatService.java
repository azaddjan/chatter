package com.azaddjan.service;

import com.azaddjan.exception.SafeGuardViolationException;
import com.azaddjan.model.Answer;
import com.azaddjan.model.Question;
import com.azaddjan.exception.NoneSenseAnswerException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("complex")
public class ComplexChatService implements ChatterInterface {
    private final static String DEFAULT_FAILURE_RESPONSE = "I'm unable to respond to that due to sensitive content. Could we rephrase or discuss something else?";
    @Value("classpath:prompts/simple_system_prompt.txt")
    Resource simple_system_prompt;
    private final ChatClient chatClient;
    private final RelevancyEvaluator evaluator;
    private final VectorStore vectorStore;

    public ComplexChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.evaluator = new RelevancyEvaluator(chatClientBuilder);
        this.vectorStore = vectorStore;
    }

    @Override
    @Retryable(
            retryFor = NoneSenseAnswerException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public Answer chat(Question question) {

        var answer = chatClient.prompt()
                .system(systemSpec -> systemSpec.text(simple_system_prompt))
                .advisors(
                        new SafeGuardAdvisor(List.of("AGI", "hate", "sig", "chevy", "45 ACP")),
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory()),
                        new QuestionAnswerAdvisor(vectorStore,
                                SearchRequest.builder()
                                        .similarityThreshold(0.7d)
                                        .topK(5)
                                        .build()
                        ),
                        new SimpleLoggerAdvisor()
                )
                .functions("currentWeather")
                .user(question.question())
                .call()
                .content();

        if (answer != null && answer.trim().equalsIgnoreCase(DEFAULT_FAILURE_RESPONSE)) {
            throw new SafeGuardViolationException(DEFAULT_FAILURE_RESPONSE);
        }

        evaluateRelevancy(question, answer);

        return new Answer(answer);
    }

    @Recover
    public Answer recover(NoneSenseAnswerException e) {
        return new Answer("I'm sorry, what I'm saying doesn't make any sense.");
    }

    @Recover
    public Answer recover(SafeGuardViolationException e) {
        return new Answer("I'm unable to respond to that due to sensitive content.");
    }


    private void evaluateRelevancy(Question question, String answer) {
        EvaluationRequest evaluationRequest =
                new EvaluationRequest(question.question(), List.of(), answer);
        EvaluationResponse evaluationResponse = evaluator.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()) {
            throw new NoneSenseAnswerException(question.question(), answer);

        }
    }
}


