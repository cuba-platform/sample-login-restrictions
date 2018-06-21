# Login Restrictions

This project highlights how developers can customize and manipulate with user login procedure in a CUBA application.

## Objective

Let’s assume that our CUBA application should be licensed as following:

* More than X concurrent users cannot access the system.
* The license has global expiration date when all users will not be able to login.

## Solution

To add custom behaviour for user [login](https://doc.cuba-platform.com/manual-6.7/login.html) we need to create `LoginEventListener` class that will be handle login events in the platform by following the specified steps:

1. In `core` module, under the main project package (in our case `com.company.loginrestrictions`) we create the security subpackage (to keep clear structure and high code readability).

2. Under the `com.company.loginrestrictions.security` package we create `LoginEventListener` class with `@Component` annotation.

3. Create method with `BeforeLoginEvent` input parameter and add `@EventListener` annotation to it. It means this method will be invoked when BeforeLoginEvent is triggered.

4. To access the license properties we use standard [configuration interfaces](https://doc.cuba-platform.com/manual-6.1/config_interfaces.html) mechanism provided by the CUBA Platform. See the `LicenseConfig` interface. It contains the default values of the license parameters.

5. The default license parameters are overridden in the `app.properties` file of the `core` module. See the `license.concurrentSessionsLimit` property.

Based on CUBA Platform 6.9.1
