package com.lock.nowait;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auxtest")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SecondaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long processed;

    private Long times;

}
