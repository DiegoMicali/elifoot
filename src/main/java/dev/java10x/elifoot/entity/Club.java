package dev.java10x.elifoot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "club")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "club_seq")
    @SequenceGenerator(name = "club_seq", sequenceName = "club_seq", allocationSize = 1)
    private Long id;

    private String name;

    private LocalDate founded;

    private String urlImg;

    private LocalDateTime createdAt;

    private Boolean active;

    @OneToOne
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @OneToMany(mappedBy = "club")
    private List<Player> players;
}
