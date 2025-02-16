package com.azaddjan.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azaddjan.entity.Document;
import com.azaddjan.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionAnswerService {

    private final EmbeddingModel embeddingModel;
    private final DocumentRepository documentRepository;

    public QuestionAnswerService(EmbeddingModel embeddingModel, DocumentRepository documentRepository) {
        this.embeddingModel = embeddingModel;
        this.documentRepository = documentRepository;
    }

    @Transactional
    public void storeDocument(String content) {
        // ✅ Check if content is empty or null before processing
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Document content cannot be empty");
        }

        try {
            // ✅ Generate embeddings safely
            List<float[]> embeddings = embeddingModel.embed(List.of(content));

            if (!embeddings.isEmpty()) {
                Document document = new Document(content, embeddings.get(0));
                documentRepository.save(document);
            } else {
                throw new RuntimeException("Failed to generate embeddings");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error generating embedding: " + e.getMessage(), e);
        }
    }

    /**
     * Stores multiple documents in batch.
     */
    @Transactional
    public void storeDocuments(List<String> contents) {
        if (contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("Document list cannot be empty");
        }

        // Remove empty or null documents before processing
        List<String> validContents = contents.stream()
                .filter(content -> content != null && !content.trim().isEmpty())
                .toList();

        if (validContents.isEmpty()) {
            throw new IllegalArgumentException("No valid documents to store");
        }

        try {
            List<float[]> embeddings = embeddingModel.embed(validContents);
            if (embeddings.size() != validContents.size()) {
                throw new RuntimeException("Mismatch between input documents and generated embeddings");
            }

            List<Document> documents = new ArrayList<>();
            for (int i = 0; i < validContents.size(); i++) {
                documents.add(new Document(validContents.get(i), embeddings.get(i)));
            }

            documentRepository.saveAll(documents); // Batch insert for efficiency
        } catch (Exception e) {
            throw new RuntimeException("Error generating embeddings for documents: " + e.getMessage(), e);
        }
    }

}
