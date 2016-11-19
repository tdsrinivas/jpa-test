package com.srini.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SlingSpecification<T> implements Specification<T> {
    private final SearchCriteria criteria;

    public SlingSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUAL:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());

            case IN:
                if (CollectionUtils.isEmpty((List)criteria.getValue())) {
                    return builder.and();
                }

                return root.get(criteria.getKey()).in(criteria.getValue());
        }

        return builder.and();
    }

    public static <T> Specification<T> noFilter() {
        return (root, query, cb) -> {
            return cb.and(); // this predicate always evaluates to True
        };
    }

    public static <T> Specifications<T> and(List<Specification<T>> specifications) {
        if (specifications.size() == 0) {
            return Specifications.where(noFilter());
        }

        Specifications<T> andedSpecs = Specifications.where(specifications.get(0));
        for (int i = 1; i < specifications.size(); i++) {
            andedSpecs = andedSpecs.and(specifications.get(i));
        }

        return andedSpecs;
    }
}
