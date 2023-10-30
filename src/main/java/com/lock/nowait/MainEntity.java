package com.lock.nowait;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "testentity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String genero;

    private String nome;
}
