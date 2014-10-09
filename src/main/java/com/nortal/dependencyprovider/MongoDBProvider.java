package com.nortal.dependencyprovider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class MongoDBProvider {

    public MongoClientURI mongoClientURI(){
        return new MongoClientURI("mongodb://kalverk:pattyparool@linus.mongohq.com:10080/patty_mongo");
    }

    public MongoClient mongoClient() {
        try {
            return new MongoClient(mongoClientURI());
        } catch (UnknownHostException e) {
            throw new RuntimeException("Problem instantiating mongo!", e);
        }
    }

    public MongoTemplate mongoTemplate() throws Exception{
        return new MongoTemplate(mongoClient(), mongoClientURI().getDatabase());
    }

}
