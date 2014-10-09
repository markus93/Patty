package com.nortal.dependencyprovider;

import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticatorImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelperImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrowdHttpAuthenticatorProvider {

    @Autowired
    private CrowdClientProvider crowdClientProvider;
    @Autowired
    private ClientPropertiesProvider clientPropertiesProvider;

    public CrowdHttpAuthenticator get() {

        return new CrowdHttpAuthenticatorImpl(
                crowdClientProvider.get(),
                clientPropertiesProvider.get(),
                CrowdHttpTokenHelperImpl.getInstance(CrowdHttpValidationFactorExtractorImpl.getInstance())
        );
    }

}