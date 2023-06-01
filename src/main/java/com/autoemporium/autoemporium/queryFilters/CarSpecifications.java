package com.autoemporium.autoemporium.queryFilters;

import com.autoemporium.autoemporium.models.Car;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecifications {
    public static Specification<Car> byModel(String model) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("model"), model);
    }

    public static Specification<Car> byPower(int power) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("power"), power);
    }

    public static Specification<Car> byId(int id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }
    public static Specification<Car> byProducer(String producer) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("producer"), producer);
    }
}
