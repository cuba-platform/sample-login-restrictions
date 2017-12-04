/*
 * Copyright (c) 2017 login-restrictions
 */

package com.company.loginrestrictions.security;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.events.BeforeLoginEvent;
import com.haulmont.cuba.security.global.LoginException;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;
import java.util.Locale;

@Component
public class LoginEventListener {

    @Inject
    private UserSessionsAPI userSessions;

    @Inject
    private LicenseConfig licenseConfig;

    @Inject
    private TimeSource timeSource;

    @Inject
    private Messages messages;

    @EventListener
    private void onBeforeLogin(BeforeLoginEvent event) throws LoginException {
        if (event.getCredentials() instanceof AbstractClientCredentials) {
            Locale locale = ((AbstractClientCredentials) event.getCredentials()).getLocale();
            if (!checkLicenseExpirationDate())
                throw new LoginException(messages.getMessage(getClass(), "LoginException.licenseOutdated", locale));
            if (!checkConcurrentUsers())
                throw new LoginException(messages.getMessage(getClass(), "LoginException.concurrentUsersLimitExceeded", locale));
        }
    }

    /**
     * @return True if user limit is not exceeded
     */
    protected boolean checkConcurrentUsers() {
        long notSystemSessionCount = userSessions.getUserSessionInfo().stream()
                .filter(s -> BooleanUtils.isFalse(s.getSystem()))
                .count();

        return notSystemSessionCount < licenseConfig.getConcurrentSessionsLimit();
    }

    /**
     * @return True if current date is before license expiration date
     */
    protected boolean checkLicenseExpirationDate() {
        Date expirationDate = new Date(licenseConfig.getLicenseExpirationDate());
        return expirationDate.after(timeSource.currentTimestamp());
    }
}
