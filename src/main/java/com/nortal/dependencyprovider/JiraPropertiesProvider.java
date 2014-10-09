package com.nortal.dependencyprovider;

import com.atlassian.crowd.service.client.ClientResourceLocator;
import org.springframework.stereotype.Service;

/*
* Gets jira client properties from jira.properties file.
* */

@Service
public class JiraPropertiesProvider {

    private ClientResourceLocator clientResourceLocator = new ClientResourceLocator("jira.properties");

    public String getJiraUri(){
        return clientResourceLocator.getProperties().getProperty("jira.base.uri");
    }

    public String getJiraProjectKey(){
        return clientResourceLocator.getProperties().getProperty("jira.project.key");
    }

    public String getJiraIssueType(){
        return clientResourceLocator.getProperties().getProperty("jira.issue.type");
    }

}
