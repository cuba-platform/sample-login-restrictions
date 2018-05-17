/*
 * Copyright (c) 2018 login-restrictions
 */
package com.company.loginrestrictions.web.screens;

import com.company.loginrestrictions.security.UserSessionExistsException;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.auth.TrustedClientCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.app.loginwindow.AppLoginWindow;

import java.util.HashMap;
import java.util.Map;

/**
 * @author subbotin
 */
public class ExtAppLoginWindow extends AppLoginWindow {
    @Override
    protected void doLogin(Credentials credentials) throws LoginException {
        String password = null;
        try {
            if (credentials instanceof LoginPasswordCredentials) {
                password = ((LoginPasswordCredentials) credentials).getPassword();
            }
            super.doLogin(credentials);
        } catch (UserSessionExistsException e) {
            if (credentials instanceof LoginPasswordCredentials) {
                String originalPassword = password;
                showOptionDialog(messages.getMainMessage("dialogs.Confirmation"), messages.getMainMessage("abortSession"),
                        Frame.MessageType.CONFIRMATION, new Action[]{
                                new DialogAction(DialogAction.Type.YES).withHandler(event ->
                                    retryLogin((LoginPasswordCredentials) credentials, originalPassword)
                                ),
                                new DialogAction(DialogAction.Type.NO, Action.Status.PRIMARY)
                        });
            }
        }
    }

    protected void retryLogin(LoginPasswordCredentials loginPasswordCredentials, String originalPassword) {
        try {
            Map<String, Object> params = loginPasswordCredentials.getParams();
            Map<String, Object> newParams = new HashMap<>();
            newParams.put("abortSession", Boolean.TRUE);
            if (params != null) {
                newParams.putAll(params);
            }
            loginPasswordCredentials.setParams(newParams);
            loginPasswordCredentials.setPassword(originalPassword);

            super.doLogin(loginPasswordCredentials);

        } catch (LoginException e1) {
            showLoginException(e1.getMessage());
        }
    }
}