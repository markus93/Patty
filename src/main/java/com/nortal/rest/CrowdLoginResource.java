package com.nortal.rest;

import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.mongodb.MongoClient;
import com.nortal.dependencyprovider.CrowdHttpAuthenticatorProvider;
import com.nortal.dependencyprovider.MongoDBProvider;
import com.nortal.model.LoginCredentials;
import com.nortal.provider.SessionCredentialsProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.atlassian.crowd.model.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/crowdLogin")
public class CrowdLoginResource {
    private Logger LOG = Logger.getLogger(CrowdLoginResource.class);

    @Autowired
    private CrowdHttpAuthenticatorProvider httpAuthenticatorProvider;

    @Autowired
    private SessionCredentialsProvider credentialsProvider;

    @Autowired
    private MongoDBProvider mongoDBProvider;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<User> save(@RequestBody LoginCredentials credentials, HttpServletRequest request, HttpServletResponse response) {

        User user = null;
        CrowdHttpAuthenticator httpAuthenticator = httpAuthenticatorProvider.get();
        try {
            user = httpAuthenticator.authenticate(request, response, credentials.username, credentials.password);
            if (httpAuthenticator.isAuthenticated(request, response)) {
                LOG.info("Authentication successful");
                credentialsProvider.add(request, credentials);
            } else {
                LOG.warn("No exception but still not authenticated!");
            }
        } catch (ApplicationPermissionException | InvalidTokenException | InactiveAccountException |
                ExpiredCredentialException | ApplicationAccessDeniedException | OperationFailedException e) {
            LOG.error("Authentication failed", e);
        } catch (InvalidAuthenticationException e) {
            LOG.info("Invalid credentials!");
        }
        return new ResponseEntity<User>(user, org.springframework.http.HttpStatus.ACCEPTED);
    }

}