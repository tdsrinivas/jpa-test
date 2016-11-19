package com.srini.controller

import com.srini.JpaTestApplication
import com.srini.model.User
import com.srini.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@SpringBootTest(classes = JpaTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest extends Specification {
    @Autowired
    UserRepository userRepo

    @Autowired
    TestRestTemplate restTemplate

    protected ParameterizedTypeReference userListType

    def setup() {
        userListType = new ParameterizedTypeReference<List<User>>(){}
    }

    def cleanup() {
        userRepo.deleteAll()
    }

    def "test find all users"() {
        setup:
        (1..10).each { i ->
            userRepo.save(new User(guid: "guid${i}", firstName: "User{i}"))
        }

        when:
        ResponseEntity<List<User>> response = restTemplate.exchange("/users", HttpMethod.GET, null, userListType);
        List<User> users = response.getBody();

        then:
        users.size() == 10
    }

    def "test find users by guids"() {
        setup:
        (1..10).each { i ->
            userRepo.save(new User(guid: "guid${i}", firstName: "User{i}"))
        }

        when:
        ResponseEntity<List<User>> response =
                restTemplate.exchange("/users?guids={guids}", HttpMethod.GET, null, userListType, "guid1,guid2");
        List<User> users = response.getBody();

        then:
        users.size() == 2
    }
}
