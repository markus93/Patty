package com.nortal.rest;

import com.nortal.dependencyprovider.JiraClientProvider;
import com.nortal.model.LoginCredentials;
import net.rcarz.jiraclient.JiraClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/secure/jiraLogin")
public class JiraLoginResource {

    @Autowired
    private JiraClientProvider jiraClientProvider;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Integer> save (@RequestBody LoginCredentials credentials){
        jiraClientProvider.createJiraClient(credentials);
        JiraClient jiraClient = jiraClientProvider.get();
        return new ResponseEntity<Integer>(1, org.springframework.http.HttpStatus.ACCEPTED);
    }

}