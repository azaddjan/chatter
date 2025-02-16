package com.azaddjan.controller;

import com.azaddjan.service.QuestionAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qa")
public class QuestionAnswerController {

    private final QuestionAnswerService qaService;

    public QuestionAnswerController(QuestionAnswerService qaService) {
        this.qaService = qaService;
    }

    /**
     * Stores a single document.
     */
    @PostMapping("/store-document")
    public ResponseEntity<String> storeDocument(@RequestBody String content) {
        try {
            qaService.storeDocument(content);
            return ResponseEntity.ok("Document stored successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error storing document: " + e.getMessage());
        }
    }

    /**
     * Stores multiple documents in batch.
     */
    @PostMapping("/store-documents")
    public ResponseEntity<String> storeDocuments(@RequestBody List<String> contents) {
        try {
            qaService.storeDocuments(contents);
            return ResponseEntity.ok("Documents stored successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error storing documents: " + e.getMessage());
        }
    }
}
