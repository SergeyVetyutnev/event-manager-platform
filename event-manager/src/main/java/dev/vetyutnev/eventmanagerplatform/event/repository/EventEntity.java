package dev.vetyutnev.eventmanagerplatform.event.repository;

import dev.vetyutnev.eventmanagerplatform.event.domain.EventStatus;
import dev.vetyutnev.eventmanagerplatform.event.registration.repository.RegistrationEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
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
    private OffsetDateTime date;

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
    @Builder.Default
    private List<RegistrationEntity> registrations = new ArrayList<>();
}
