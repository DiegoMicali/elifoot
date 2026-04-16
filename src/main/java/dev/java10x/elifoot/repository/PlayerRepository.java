package dev.java10x.elifoot.repository;

import dev.java10x.elifoot.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
