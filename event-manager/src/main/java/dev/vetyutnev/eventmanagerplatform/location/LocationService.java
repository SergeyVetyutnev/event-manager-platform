package dev.vetyutnev.eventmanagerplatform.location;

import dev.vetyutnev.eventmanagerplatform.location.exception.LocationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Transactional
    public Location create(Location locationDomain){
        log.info("Создание новой локации {}", locationDomain.name());

        var entity = locationMapper.toEntity(locationDomain);
        entity.setId(null);
        var savedEntity = locationRepository.save(entity);

        return locationMapper.toDomain(savedEntity);
    }

    public List<Location> getAll(){
        log.info("Запрос списка всех локаций");
        return locationRepository.findAll().stream()
                .map(locationMapper::toDomain)
                .toList();
    }

    public Location getLocationById(Long id){
        log.info("Запрос локации по id: {}", id);
        LocationEntity entity = getEntityByIdOrThrow(id);
        return locationMapper.toDomain(entity);
    }

    @Transactional
    public Location update( Long id, Location locationDomain){
        log.info("Обновление локации с id: {}", id);

        var locationToUpdate = getEntityByIdOrThrow(id);
        locationMapper.updateEntityFromDto(locationDomain, locationToUpdate);
        return locationMapper.toDomain(locationToUpdate);
    }

    @Transactional
    public void delete(Long id){
        log.info("Удаление локации с id: {}", id);

        locationRepository.delete(getEntityByIdOrThrow(id));
    }

    private LocationEntity getEntityByIdOrThrow(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException("Локация с id " + id + " не найдена"));
    }




}
