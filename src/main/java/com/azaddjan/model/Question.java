package com.azaddjan.model;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record Question (@NotBlank(message = "Question is required")String question)  implements Serializable {
}
