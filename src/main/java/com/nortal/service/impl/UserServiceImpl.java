package com.nortal.service.impl;

import com.atlassian.crowd.embedded.api.SearchRestriction;
import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.search.builder.Restriction;
import com.atlassian.crowd.search.query.entity.restriction.constants.UserTermKeys;
import com.nortal.dependencyprovider.CrowdClientProvider;
import com.nortal.model.PattyUser;
import com.nortal.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
    TODO
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    CrowdClientProvider crowdClientProvider;

    private Logger LOG = Logger.getLogger(UserServiceImpl.class);

    @Override
    public List<PattyUser> search(String keyword) {
        List<PattyUser> pattyUserList = new ArrayList<>();
        try {
            //SearchRestriction firstName = Restriction.on(UserTermKeys.FIRST_NAME).startingWith(keyword);
            //SearchRestriction lastName = Restriction.on(UserTermKeys.LAST_NAME).startingWith(keyword);
            SearchRestriction displayName = Restriction.on(UserTermKeys.DISPLAY_NAME).startingWith(keyword);
            List<User> displayNameList = crowdClientProvider.get().searchUsers(displayName,0,5);
            for (int i=0;i<displayNameList.size();i++){
                pattyUserList.add(new PattyUser(displayNameList.get(i)));
            }

        } catch (ApplicationPermissionException e) {
            LOG.info("ApplicationPermissionExceptio");
        } catch (OperationFailedException e) {
            LOG.info("OperationFailedException");
        } catch (InvalidAuthenticationException e) {
            LOG.info("InvalidAuthenticationException");
        }
        LOG.info(pattyUserList.toString());
        return pattyUserList;
    }
}
