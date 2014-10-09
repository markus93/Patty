package com.nortal.model;

import com.atlassian.crowd.model.user.User;

/*
    Do we need that much information for PattyUser maybe displayname and username is enough
 */

public class PattyUser {

    public String username;
    public String name;
    public String firstname;
    public String lastname;

    public PattyUser() {
    }

    public PattyUser(User crowdUser) {
        this.username = crowdUser.getName();
        this.name = crowdUser.getDisplayName();
        this.firstname = crowdUser.getFirstName();
        this.lastname = crowdUser.getLastName();
    }

    @Override
    public String toString() {
        return "PattyUser{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
