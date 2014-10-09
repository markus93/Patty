package com.nortal.dependencyprovider;

import com.atlassian.crowd.service.client.ClientProperties;
import com.atlassian.crowd.service.client.ClientPropertiesImpl;
import com.atlassian.crowd.service.client.ClientResourceLocator;
import org.springframework.stereotype.Service;

/*
* Gets crowd connection properties from crowd.properties file.
* */
@Service
public class ClientPropertiesProvider {

    public ClientProperties get() {
        ClientResourceLocator clientResourceLocator = new ClientResourceLocator("crowd.properties");
        return ClientPropertiesImpl.newInstanceFromResourceLocator(clientResourceLocator);
    }
}