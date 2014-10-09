package com.nortal.provider.impl;

import com.nortal.dependencyprovider.CrowdHttpAuthenticatorProvider;
import com.nortal.model.LoginCredentials;
import com.nortal.provider.SessionCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionCredentialsProviderImpl implements SessionCredentialsProvider {

    @Autowired
    private CrowdHttpAuthenticatorProvider httpAuthenticatorProvider;

    private Map<String, LoginCredentials> credentials = new HashMap<>();

    @Override
    public void remove(ServletRequest request) {
        credentials.remove(httpAuthenticatorProvider.get().getToken((HttpServletRequest) request));
    }

    @Override
    public LoginCredentials get(ServletRequest request) {
        return credentials.get(httpAuthenticatorProvider.get().getToken((HttpServletRequest) request));
    }

    @Override
    public void add(ServletRequest request, LoginCredentials credentials) {
        this.credentials.put(httpAuthenticatorProvider.get().getToken((HttpServletRequest) request), credentials);
    }
}