package com.nortal.rest;

import com.nortal.model.PattyUser;
import com.nortal.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/secure/people")
public class PeopleResource {

    @Autowired
    private UserService userService;

    private Logger LOG = Logger.getLogger(PeopleResource.class);

    @RequestMapping(method = RequestMethod.GET, value = "/search/{searchTerm}" ,produces = "application/json")
    public ResponseEntity<List<PattyUser>> searchUser(@PathVariable String searchTerm){
        return new ResponseEntity<List<PattyUser>>(userService.search(searchTerm), org.springframework.http.HttpStatus.ACCEPTED);
    }

}
