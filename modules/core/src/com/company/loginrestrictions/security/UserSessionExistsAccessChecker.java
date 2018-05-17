/*
 * Copyright (c) 2018 login-restrictions
 */

package com.company.loginrestrictions.security;

import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.UserAccessChecker;
import com.haulmont.cuba.security.global.LoginException;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Objects;

@Component("loginrestrictions_UserSessionExistsAccessChecker")
public class UserSessionExistsAccessChecker implements UserAccessChecker, Ordered {
    @Inject
    protected UserSessionsAPI userSessions;

    @Override
    public void check(Credentials credentials, AuthenticationDetails authenticationDetails) throws LoginException {
        if (credentials instanceof AbstractClientCredentials) {
            AbstractClientCredentials clientCredentials = (AbstractClientCredentials) credentials;
            if (clientCredentials.getParams() != null && Boolean.TRUE.equals(clientCredentials.getParams().get("abortSession"))) {
                userSessions.getUserSessionsStream()
                        .filter(s -> !s.isSystem() && Objects.equals(s.getUser().getLogin(), clientCredentials.getUserIdentifier()))
                        .findAny()
                        .ifPresent(s -> userSessions.killSession(s.getId()));
            }
            if (checkExistsUser(clientCredentials.getUserIdentifier()))
                throw new UserSessionExistsException(clientCredentials.getUserIdentifier());
        }
    }

    protected boolean checkExistsUser(String login) {
        return userSessions.getUserSessionsStream()
                .anyMatch(s -> !s.isSystem() && Objects.equals(s.getUser().getLogin(), login));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE;
    }
}
