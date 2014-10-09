package com.nortal.rest;

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.search.query.entity.restriction.TermRestriction;
import com.atlassian.crowd.search.query.entity.restriction.constants.UserTermKeys;
import com.nortal.dependencyprovider.CrowdClientProvider;
import com.nortal.dependencyprovider.JiraClientProvider;
import com.nortal.dependencyprovider.JiraPropertiesProvider;
import com.nortal.model.LoginCredentials;
import com.nortal.model.Pat;
import com.nortal.provider.SessionCredentialsProvider;
import com.nortal.repository.PatService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import net.rcarz.jiraclient.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/secure/createIssue")
public class PatResource {

    private final Logger LOG = Logger.getLogger(PatResource.class);

    @Autowired
    private JiraClientProvider jiraClientProvider;

    @Autowired
    private JiraPropertiesProvider jiraPropertiesProvider;

    @Autowired
    private CrowdClientProvider crowdClientProvider;

    @Autowired
    private SessionCredentialsProvider credentialsPovider;

    @Autowired
    private PatService patService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Issue> createIssue(@RequestBody Pat pat, HttpServletRequest request){

        LoginCredentials loginCredentials = credentialsPovider.get(request);
        Issue createdIssue = null;
        HttpStatus httpStatus = null;

        if (isValidPat(pat, loginCredentials)) {
            createdIssue = createIssueOnJira(pat, loginCredentials, jiraClientProvider.get());
            if (createdIssue != null) {
                persistPat(pat);
                LOG.info("Issue created successfully");
                LOG.info(createdIssue);
                httpStatus = org.springframework.http.HttpStatus.ACCEPTED;
            }
            httpStatus = HttpStatus.NO_CONTENT;
        } else {
            LOG.warn("Submitted Pat from user " + loginCredentials.username + " is not valid. Probably hacked!");
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Issue>(createdIssue, httpStatus);
    }

    private boolean isValidPat(Pat pat, LoginCredentials creds) {
        User submitter = getUserFromRequest(creds);
        return creds.username.equals(pat.fromUser)
                && (submitter.getFirstName() + " " + submitter.getLastName()).equals(pat.fromName);
    }

    private User getUserFromRequest(LoginCredentials userCreds) {
        try {
            return crowdClientProvider.get().searchUsers(new TermRestriction<String>(UserTermKeys.USERNAME, userCreds.username), 0, 1).get(0);
        } catch (OperationFailedException | ApplicationPermissionException | InvalidAuthenticationException e) {
            LOG.error(e);
        }
        return null;
    }

    private Issue createIssueOnJira(Pat pat, LoginCredentials userCreds, JiraClient jira) {
        Issue createdIssue = null;
        try {
            createdIssue = jira.createIssue(jiraPropertiesProvider.getJiraProjectKey(), jiraPropertiesProvider.getJiraIssueType())
                    .field(Field.SUMMARY, String.format("A pat from %s to %s", pat.fromName, pat.toName))
                    .field(Field.DESCRIPTION, pat.description)
                    .field(Field.REPORTER, userCreds.username)
                    .field(Field.ASSIGNEE, pat.toUser)
                    .execute();
        } catch (JiraException e) {
            LOG.error("Issue creation failed!", e);
        }
        return createdIssue;
    }

    private void persistPat(Pat pat) {
        pat.submitDate = new Date();
        patService.insert(pat);
    }

}
