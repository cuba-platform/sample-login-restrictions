/*
 * Copyright (c) 2018 login-restrictions
 */

package com.company.loginrestrictions.security;

import com.haulmont.cuba.core.global.Logging;
import com.haulmont.cuba.core.global.SupportedByClient;
import com.haulmont.cuba.security.global.LoginException;

@SupportedByClient
@Logging(Logging.Type.BRIEF)
public class UserSessionExistsException extends LoginException {
    protected String login;

    public UserSessionExistsException(String login) {
        super("User session exists");
        this.login = login;
    }
}
