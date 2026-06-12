package dev.vetyutnev.eventmanagerplatform.event;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<EventEntity> withFilter(EventSearchRequestDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            //имя
            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(cb.equal(root.get("name"), filter.name()));
            }
//          поиск по подстроке
//            if (filter.name() != null && !filter.name().isBlank()) {
//                String pattern = "%" + filter.name().toLowerCase() + "%";
//
//                predicates.add(cb.like(cb.lower(root.get("name")), pattern));
//            }


            //вместимость
            if (filter.placesMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxPlaces"), filter.placesMin()));
            }
            if (filter.placesMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("maxPlaces"), filter.placesMax()));
            }

            //даты
            if (filter.dateStartAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.dateStartAfter()));
            }
            if (filter.dateStartBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.dateStartBefore()));
            }

            //стоимость
            if (filter.costMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("cost"), filter.costMin()));
            }
            if (filter.costMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("cost"), filter.costMax()));
            }

            //длительность
            if (filter.durationMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("duration"), filter.durationMin()));
            }
            if (filter.durationMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("duration"), filter.durationMax()));
            }

            //локация и статус
            if (filter.locationId() != null) {
                predicates.add(cb.equal(root.get("locationId"), filter.locationId()));
            }
            if (filter.eventStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.eventStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
