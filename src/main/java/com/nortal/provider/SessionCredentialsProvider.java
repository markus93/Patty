package com.nortal.provider;

import com.nortal.model.LoginCredentials;
import javax.servlet.ServletRequest;

public interface SessionCredentialsProvider {
    LoginCredentials get(ServletRequest request);

    void add(ServletRequest request, LoginCredentials credentials);

    void remove(ServletRequest request);
}
