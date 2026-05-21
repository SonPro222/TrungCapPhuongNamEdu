package org.example.trungcapphuongnam.common.spec;


import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class LocJpa {

    private LocJpa() {
    }

    public static <T> Specification<T> empty() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static <T> Specification<T> eq(String field, Object value) {
        return (root, query, cb) -> {
            if (value == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get(field), value);
        };
    }

    public static <T> Specification<T> like(String field, String value) {
        return (root, query, cb) -> {
            if (isBlank(value)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%");
        };
    }

    public static <T> Specification<T> keyword(String value, String... fields) {
        return (root, query, cb) -> {
            if (isBlank(value) || fields == null || fields.length == 0) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            String pattern = "%" + value.trim().toLowerCase() + "%";

            for (String field : fields) {
                predicates.add(cb.like(cb.lower(root.get(field)), pattern));
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> gte(String field, Comparable value) {
        return (root, query, cb) -> {
            if (value == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get(field), value);
        };
    }

    public static <T> Specification<T> lte(String field, Comparable value) {
        return (root, query, cb) -> {
            if (value == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get(field), value);
        };
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}