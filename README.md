# fhir.med-united.health

## Prerequisites

In order to start developing, you should have:
- Oracle Java (JDK) installed: Minimum JDK8 or newer.
- Apache Maven build tool (newest version)

## Running locally

There are different ways to run this server and it depends on your environment requirements:

## Using jetty
```bash
mvn jetty:run
```


If you need to run this server on a different port (using Maven), you can change the port in the run command as follows:

```bash
mvn -Djetty.port=8888 jetty:run
```

Server will then be accessible at http://localhost:8888/ and eg. http://localhost:8888/fhir/metadata. Remember to adjust you overlay configuration in the application.yaml to eg.

```yaml
    tester:
      -
          id: home
          name: Local Tester
          server_address: 'http://localhost:8888/fhir'
          refuse_to_fetch_third_party_urls: false
          fhir_version: R4
```

### Using Spring Boot with :run
```bash
mvn clean spring-boot:run -Pboot
```
NOTE: For development purposes you can create an application-dev.yaml file and configure it as needed. To run the server with dev profile, execute:
```
mvn clean spring-boot:run -Pdev
```

## Configurations

By default, this project is configured to use H2 as the database.

### MySql configuration

To configure the server to use MySQL, instead of the default H2, update the application.yaml file to have the following:

```yaml
spring:
  datasource:
    url: 'jdbc:mysql://localhost:3306/hapi_dstu3'
    username: admin
    password: admin
    driverClassName: com.mysql.jdbc.Driver
```

Also, make sure you are not setting the Hibernate dialect explicitly, in other words remove any lines similar to:

```
hibernate.dialect: {some none MySQL dialect}
```

On some systems, it might be necessary to override hibernate's default naming strategy. The naming strategy must be set using spring.jpa.hibernate.physical_naming_strategy.

```yaml
spring:
  jpa:
    hibernate.physical_naming_strategy: NAME_OF_PREFERRED_STRATEGY
```
On linux systems or when using docker mysql containers, it will be necessary to review the case-sensitive setup for
mysql schema identifiers. See  https://dev.mysql.com/doc/refman/8.0/en/identifier-case-sensitivity.html. We suggest you
set `lower_case_table_names=1` during mysql startup.

### PostgreSQL configuration

To configure the server to use PostgreSQL, instead of the default H2, update the application.yaml file to have the following:

```yaml
spring:
  datasource:
    url: 'jdbc:postgresql://localhost:5432/hapi_dstu3'
    username: admin
    password: admin
    driverClassName: org.postgresql.Driver
```

