[![Build](https://github.com/pawl1n/ks-java/actions/workflows/build.yml/badge.svg)](https://github.com/pawl1n/ks-java/actions/workflows/build.yml)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=pawl1n_ks-java&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=pawl1n_ks-java)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pawl1n_ks-java&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pawl1n_ks-java)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=pawl1n_ks-java&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=pawl1n_ks-java)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=pawl1n_ks-java&metric=bugs)](https://sonarcloud.io/summary/new_code?id=pawl1n_ks-java)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=pawl1n_ks-java&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=pawl1n_ks-java)

# E-commerce Backend API

This project provides the backend API for an e-commerce website.

It is built using Spring Boot and provides RESTful API with the HATEOAS.

**The project is still under development, so not all features have been fully implemented.**

## Getting Started

To get started with the project, clone the repository and import it into your preferred IDE.

The project uses [Maven](https://maven.apache.org/) for dependency management, so you must have Maven installed on your
system.

### Running project

To run the project, you should set up environment variables for the [database](#database) and [CDN](#saving-images).

You can then run the application using the following command:

```
mvn spring-boot:run
```

The application will start up and listen for requests on <http://localhost:8080>.

It is possible to start the project using Docker with following command:

```
mvn clean install && docker-compose up
```

## Database

The project uses [Liquibase](https://www.liquibase.com/) for database version control.
Liquibase-related tables are stored in the `public` schema, while the actual data is stored in the `main` schema.

[PostgreSQL](https://www.postgresql.org/) was chosen as an RDBMS.

To connect the project to the database, you should insert these environment variables:

* `PGUSER` - Database user
* `PGPASSWORD` - Password
* `PGHOST` - Hostname (for example, `localhost`)
* `PGPORT` - Port (5432 by default)
* `PGDATABASE` - Database name

## Saving Images

All the created images are stored in the [Cloudinary](https://cloudinary.com/).

The end client will receive a link to the saved image transformed into
the [WebP](https://developers.google.com/speed/webp/) format.

To set up Cloudinary, you should insert this environment variable:

* `CLOUDINARY_URL` - Url from Cloudinary

## Testing

To run unit tests, you can run this command:

```
mvn test
```

The integration tests use testcontainers. For testing, Docker should be installed and started.
