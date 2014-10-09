package com.nortal.dependencyprovider;

import com.nortal.model.LoginCredentials;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.JiraClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JiraClientProvider {

    @Autowired
    private JiraPropertiesProvider jiraPropertiesProvider;

    private JiraClient jiraClient;

    public void createJiraClient(LoginCredentials loginCredentials){
        ICredentials creds = new BasicCredentials(loginCredentials.username, loginCredentials.password);
        jiraClient =  new JiraClient(jiraPropertiesProvider.getJiraUri(), creds);
    }

    public JiraClient get() {
        return jiraClient;
    }

}
