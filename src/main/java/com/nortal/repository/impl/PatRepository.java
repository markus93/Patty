package com.nortal.repository.impl;

import com.nortal.model.Pat;
import com.nortal.repository.PatService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatRepository extends MongoRepository implements PatService {

    @Override
    public List<Pat> findAll() {
        return null;
    }

    @Override
    public void insert(Pat pat) {
       getTemplate().insert(pat);
    }

}
