package dev.vetyutnev.eventmanagerplatform.event;

import dev.vetyutnev.eventmanagerplatform.registration.RegistrationEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;

    @Column(name = "occupied_places", nullable = false)
    private Integer occupiedPlaces;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistrationEntity> registrations = new ArrayList<>();
}
