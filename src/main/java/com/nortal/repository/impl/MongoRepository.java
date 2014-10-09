package com.nortal.repository.impl;

import com.nortal.dependencyprovider.MongoDBProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoRepository {

    private Logger LOG = Logger.getLogger(MongoRepository.class);

    @Autowired
    private MongoDBProvider mongoDBProvider;

    protected MongoTemplate getTemplate() {
        try{
            return mongoDBProvider.mongoTemplate();
        }catch (Exception e){
            LOG.info("Unable to return mongoTemplate");
        }
        return null;
    }
}
