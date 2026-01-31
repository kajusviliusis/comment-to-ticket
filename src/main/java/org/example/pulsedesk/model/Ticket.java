package org.example.pulsedesk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private String priority;
    private String shortSummary;

    private LocalDateTime createdAt =  LocalDateTime.now();

    @OneToOne
    private Comment comment;
}
