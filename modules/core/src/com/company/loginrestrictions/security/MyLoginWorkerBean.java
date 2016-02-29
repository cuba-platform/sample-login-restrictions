package com.company.loginrestrictions.security;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.security.app.LoginWorkerBean;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MyLoginWorkerBean extends LoginWorkerBean {

    @Inject
    private UserSessionsAPI userSessions;

    @Inject
    private LicenseConfig licenseConfig;

    @Inject
    protected TimeSource timeSource;

    @Override
    public UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {

        if (!checkLicenseExpirationDate())
            throw new LoginException(messages.getMessage(getClass(), "LoginException.licenseOutdated", locale));

        if (!checkConcurrentUsers())
            throw new LoginException(messages.getMessage(getClass(), "LoginException.concurrentUsersLimitExceeded", locale));

        return super.login(login, password, locale, params);

    }

    @Override
    public UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> params) throws LoginException {
        if (!checkLicenseExpirationDate())
            throw new LoginException(messages.getMessage(getClass(), "LoginException.licenseOutdated", locale));

        if (!checkConcurrentUsers())
            throw new LoginException(messages.getMessage(getClass(), "LoginException.concurrentUsersLimitExceeded", locale));

        return super.loginByRememberMe(login, rememberMeToken, locale, params);
    }

    /**
     * @return True if user limit is not exceeded
     */
    protected boolean checkConcurrentUsers() {
        return userSessions.getUserSessionInfo().size() < licenseConfig.getConcurrentSessionsLimit();
    }

    /**
     * @return True if current date is before license expiration date
     */
    protected boolean checkLicenseExpirationDate() {
        Date expirationDate = new Date(licenseConfig.getLicenseExpirationDate());
        return expirationDate.after(timeSource.currentTimestamp());
    }
}
