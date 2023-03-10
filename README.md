# E-commerce Backend API

This project provides the backend API for an e-commerce website.

It is built using Spring Boot and provides RESTful API with the HATEOAS.

**The project is still under development, so not all features have been fully implemented.**

## Getting Started

To get started with the project, clone the repository and import it into your preferred IDE.

The project uses [Maven](https://maven.apache.org/) for dependency management, so you must have Maven installed on your system.

### Environment Variables

To run the project, you should set up environment variables for the [database](#Database) and [CDN](#Images).

You can then run the application using the following command:
```
mvn spring-boot:run
```

The application will start up and listen for requests on <http://localhost:8080>.

## API Endpoints

The API provides endpoints for managing products, categories, and images.

### The following endpoints are currently available:

#### Products

`GET /api/products` - Retrieves a list of all products.

`GET /api/products/{id}` - Retrieves a specific product by ID.

`POST /api/products` - Creates a new product.

`PUT /api/products/{id}` - Updates an existing product.

`DELETE /api/products/{id}` - Deletes a product by ID.

`GET /api/products/{id}/category` - Retrieves a category of the product.

#### Categories

`GET /api/categories` - Retrieves a list of all categories.

`GET /api/categories/{id}` - Retrieves a specific category by ID.

`POST /api/categories` - Creates a new category.

`PUT /api/categories/{id}` - Updates an existing category.

`DELETE /api/categories/{id}` - Deletes a category by ID.

#### Images

`GET /api/images` - Retrieves a list of all images.

`GET /api/images/{id}` - Retrieves a specific image by ID.

`POST /api/images` - Creates a new image. Details in the [Images](#images) section

`PUT /api/images/{id}` - Updates an existing image.

`DELETE /api/images/{id}` - Deletes an image by ID.

#### Authentication

`POST /api/auth/login` - Login user and generate JWT token.

`POST /api/auth/register` - Register new user with role user.

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


## Images

All the created images are stored in the [Cloudinary](https://cloudinary.com/).

The end client will receive a link to the saved image transformed into the [WebP](https://developers.google.com/speed/webp/) format.

To setup Cloudinary, you should insert these environment variables:

* `CDN_CLOUD_NAME` - Cloud name
* `CDN_API_KEY` - Api key
* `CDN_API_SECRET` - Api secret

## Testing
To run unit tests, you can run this command:
```
mvn test
```

The integration tests use an H2 database. Need to be started manually with JUnit5.
