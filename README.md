# Login Restrictions

This project highlights how developers can customize and manipulate with user login procedure in [CUBA](https://www.cuba-platform.com/).

## Objective

Let’s assume that our CUBA application should be licensed as following:
* More than X concurrent users cannot access the system.
* The license has global expiration date when all users will not be able to login.

## Solution

To add custom behaviour for user login we need to [extend](https://doc.cuba-platform.com/manual-6.1/bean_extension.html) the `LoginWorkerBean` declared in the platform by following the specified steps:

1. In `core` module, under the main project package (in our case `com.company.loginrestrictions`) we create the security subpackage (to keep clear structure and high code readability).

2. Under the `com.company.loginrestrictions.security` package we create `MyLoginWorkerBean` to extend the platform’s implementation of the `LoginWorkerBean` and override `login()` and `loginByRememberMe()` methods.

3. To replace the original worker implementation with our `MyLoginWorkerBean` we register our class in `spring.xml` of the `core` module by adding: `<bean id="cuba_LoginWorker" class="com.company.loginworkersample.security.MyLoginWorkerBean"/>`. Now the overridden methods of the `MyLoginWorkerBean` class will be used when user logs in.

4. To access the license properties we use standard [configuration interfaces](https://doc.cuba-platform.com/manual-6.1/config_interfaces.html) mechanism provided by the CUBA Platform. See the `LicenseConfig` interface. It contains the default values of the license parameters.

5. The default license parameters are overridden in the `app.properties` file of the `core` module. See the `license.concurrentSessionsLimit` property.

Based on CUBA Platform 6.1.4
