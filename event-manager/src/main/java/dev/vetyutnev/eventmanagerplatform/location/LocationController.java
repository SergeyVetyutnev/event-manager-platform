package dev.vetyutnev.eventmanagerplatform.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @PostMapping
    public ResponseEntity<LocationDto> create(@Valid @RequestBody LocationDto request){
        log.info("HTTP запрос на создание локации {}", request.name());

        var createdLocation = locationService.create(
                locationMapper.toDomain(request)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationMapper.toDto(createdLocation)
        );

    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAll(){
        log.info("HTTP запрос на получение всех локаций");

        List<LocationDto> locations = locationService.getAll().stream()
                .map(locationMapper::toDto)
                .toList();

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getById(@PathVariable Long locationId){
        log.info("HTTP запрос на получение локации с id: {}", locationId);

        var location = locationService.getLocationById(locationId);

        return ResponseEntity.ok(
                locationMapper.toDto(location)
        );
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> update(
            @PathVariable Long locationId,
            @Valid @RequestBody LocationDto request){
        log.info("HTTP запрос на обновление локации с id: {}", locationId);

        var updatedLocation = locationService.update(
                locationId,
                locationMapper.toDomain(request)
        );

        return ResponseEntity.ok(
                locationMapper.toDto(updatedLocation)
        );
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> delete(@PathVariable Long locationId){
        log.info("HTTP запрос на удаление локации с id: {}", locationId);

        locationService.delete(locationId);

        return ResponseEntity.noContent().build();
    }
}
