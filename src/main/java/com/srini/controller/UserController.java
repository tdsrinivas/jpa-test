package com.srini.controller;

import com.srini.model.User;
import com.srini.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepo;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<User> getAll(@RequestParam(required = false) List<String> guids) {
        Pageable pageable = new PageRequest(0, 10);

        List<Specification<User>> specifications = new ArrayList<>();

        specifications.add(anyExactText("guid", guids));

        Page<User> users = userRepo.findAll(andSpecs(specifications), pageable);
        return users.getContent();
    }

    public static <T> Specification<T> anyExactText(String property, List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return noFilter();
        }

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return root.get(property).in(values);
            }
        };
    }

    public static <T> Specification<T> noFilter() {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(); // this predicate always evaluates to True
            }
        };
    }

    public static <T> Specifications<T> andSpecs(List<Specification<T>> specifications) {
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
