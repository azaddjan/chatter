package com.azaddjan.exception;

public class NoneSenseAnswerException extends RuntimeException {
    public NoneSenseAnswerException(String question, String answer) {
        super(String.format("The answer '%s' is not relevant to the question '%s'.", answer, question));
    }
}
