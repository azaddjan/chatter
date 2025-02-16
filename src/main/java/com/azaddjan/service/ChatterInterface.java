package com.azaddjan.service;

import com.azaddjan.model.Answer;
import com.azaddjan.model.Question;

public interface ChatterInterface {
    Answer chat(Question prompt);
}
