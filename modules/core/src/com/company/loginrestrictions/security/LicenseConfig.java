package com.company.loginrestrictions.security;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultLong;

@Source(type = SourceType.APP)
public interface LicenseConfig extends Config {

    /**
     *  @return Returns a number of concurrent user sessions allowed by license.
     *  Default value is 3.
     */
    @Property("license.concurrentSessionsLimit")
    @DefaultInt(3)
    Integer getConcurrentSessionsLimit();

    /**
     * @return Returns DateTime in milliseconds when license will be expired.
     * Default value is 1483228800000L, equals to 01/01/2017
     */
    @Property("license.expirationDate")
    @DefaultLong(1483228800000L) //01/01/2017
    Long getLicenseExpirationDate();
}
