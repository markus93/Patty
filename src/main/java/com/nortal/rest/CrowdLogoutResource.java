package com.nortal.rest;

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.nortal.dependencyprovider.CrowdHttpAuthenticatorProvider;
import com.nortal.provider.SessionCredentialsProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/secure/crowdLogout")
public class CrowdLogoutResource {
    private Logger LOG = Logger.getLogger(CrowdLogoutResource.class);

    @Autowired
    private CrowdHttpAuthenticatorProvider httpAuthenticatorProvider;
    @Autowired
    private SessionCredentialsProvider credentialsPovider;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Integer> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            httpAuthenticatorProvider.get().logout(request, response);
            credentialsPovider.remove(request);
        } catch (OperationFailedException | InvalidAuthenticationException | ApplicationPermissionException e) {
            LOG.error("Logout failed");
            return new ResponseEntity<Integer>(0, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Integer>(1, HttpStatus.ACCEPTED);
    }

}