package com.nortal.repository;

import com.nortal.model.Pat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PatService {

    List<Pat> findAll();

    //@Secured("ROLE_jira-developers")
    void insert(Pat pat);

}
