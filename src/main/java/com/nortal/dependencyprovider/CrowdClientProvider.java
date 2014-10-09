package com.nortal.dependencyprovider;

import com.atlassian.crowd.integration.rest.service.RestCrowdClient;
import com.atlassian.crowd.service.client.CrowdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrowdClientProvider {
    @Autowired
    private ClientPropertiesProvider clientPropertiesProvider;

    public CrowdClient get() {
        return new RestCrowdClient(clientPropertiesProvider.get());
    }
}
