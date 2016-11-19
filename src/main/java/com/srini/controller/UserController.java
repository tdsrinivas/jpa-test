package com.srini.controller;

import com.srini.model.User;
import com.srini.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.srini.controller.SearchCriteria.Operation.IN;
import static com.srini.controller.SlingSpecification.and;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepo;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<User> getAll(@RequestParam(required = false) List<String> guids) {
        Pageable pageable = new PageRequest(0, 10);

        List<Specification<User>> specs = new ArrayList<>();
        specs.add(new SlingSpecification<>(SearchCriteria.builder().key("guid").operation(IN).value(guids).build()));

        Page<User> users = userRepo.findAll(and(specs), pageable);
        return users.getContent();
    }
}
