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

## API Endpoints

The API provides endpoints for managing products, categories, and images.

### The following endpoints are currently available:

<details>
<summary>Products</summary>

`GET /api/products` - Retrieves a list of all products.

Params:

| Param        | Description                                                                                        |
|--------------|----------------------------------------------------------------------------------------------------|
| categoryPath | Search products by category path.<br/>Finds products even if they belong to a descendant category. |

`GET /api/products/{id}` - Retrieves a specific product by ID.

`GET /api/products/slug/{slug}` - Retrieves a specific product by slug.

`POST /api/products` - Creates a new product.

`PUT /api/products/{id}` - Updates an existing product.

`DELETE /api/products/{id}` - Deletes a product by ID.

`GET /api/products/{id}/category` - Retrieves a category of the product.

</details>

<details>
<summary>Product variations</summary>

`GET /api/products/{id}/variations` - Retrieves a list of all product variationss.

`GET /api/products/{id}/variations/{id}` - Retrieves a specific product variation by ID.

`POST /api/products/id}/variations` - Creates a new product variation.

`PUT /api/products/{id}/variations/{id}` - Updates an existing product variation.

`DELETE /api/products/{id}/variations/{id}` - Deletes a product variation by ID.

</details>

<details>
<summary>Categories</summary>

`GET /api/categories` - Retrieves a list of all categories.

`GET /api/categories/root` - Retrieves a list of all root categories.

`GET /api/categories/tree` - Retrieves a tree of all categories.

`GET /api/categories/{id}` - Retrieves a specific category by ID.

`GET /api/categories/path/{*path}` - Retrieves a specific category by its path.

`GET /api/categories/{id}/descendants` - Retrieves descendants of category

`POST /api/categories` - Creates a new category.

`PUT /api/categories/{id}` - Updates an existing category.

`DELETE /api/categories/{id}` - Deletes a category by ID.

</details>

<details>
<summary>Orders</summary>

WIP

</details>

<details>
<summary>Images</summary>

`GET /api/images` - Retrieves a list of all images.

`GET /api/images/{id}` - Retrieves a specific image by ID.

`POST /api/images` - Creates a new image. Details in the [Images](#saving-images) section

`PUT /api/images/{id}` - Updates an existing image.

`DELETE /api/images/{id}` - Deletes an image by ID.

</details>


<details>
<summary>Variations</summary>

`GET /api/variations` - Retrieves a list of all variations.

`GET /api/variations/{id}` - Retrieves a specific variation by ID.

`POST /api/variations` - Creates a new variation.

`PUT /api/variations/{id}` - Updates an existing variation.

`DELETE /api/variations/{id}` - Deletes a variation by ID.

</details>

<details>
<summary>Variation options</summary>

`GET /api/variations/{id}/options` - Retrieves a list of all variation options.

`GET /api/variations/{id}/options/{id}` - Retrieves a specific variation option by ID.

`POST /api/variations/id}/options` - Creates a new variation option.

`PUT /api/variations/{id}/options/{id}` - Updates an existing variation option.

`DELETE /api/variations/{id}/options/{id}` - Deletes a variation option by ID.

</details>

<details>
<summary>Authentication</summary>

`POST /api/auth/login` - Login user and generate JWT tokens.

`POST /api/auth/register` - Register new user with role user.

`POST /api/auth/refresh` - Refresh access token.

</details>

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
