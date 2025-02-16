package com.azaddjan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonType;
import java.util.Map;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "vector(768)", nullable = false)
    private float[] embedding;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> metadata;

    public Document() {}

    public Document(String content, float[] embedding) {
        this.content = content;
        this.embedding = embedding;
        this.metadata = Map.of(); // Default to empty JSON object
    }

    public Document(String content, float[] embedding, Map<String, Object> metadata) {
        this.content = content;
        this.embedding = embedding;
        this.metadata = metadata;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public float[] getEmbedding() { return embedding; }
    public void setEmbedding(float[] embedding) { this.embedding = embedding; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", embedding=" + embedding +
                ", metadata=" + metadata +
                '}';
    }
}
