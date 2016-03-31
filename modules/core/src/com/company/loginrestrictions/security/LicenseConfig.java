/*
 * Copyright (c) 2016 Haulmont
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
