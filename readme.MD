# Video Rental Store

REST API for renting out movies built with Spring Boot 2 and Spring Data JPA.
When running this application with the `dev` profile, several films and one customer will be added to the database.


Using this API you will be able to:

- View a list of customers
- View a list of films
- Rent out one or more films
- Return one or more films and gain bonus points

## Running the application

- Clone this repository
- Run `mvn clean package`, this will package the application as an executable JAR and run it 

## Using the API

- See restdocs at [http://localhost:4200/docs/index.html]
- Alternatively import [this Postman collection](https://www.getpostman.com/collections/6a4cd9bf4a44f8a934e6) 

## Choices

Below I will document several of the choices I've made in the process of building this application 


### Spring 

I've worked a lot with Spring and Spring Boot over the last year. 
This, as well as the autoconfiguration of Spring Boot and wide community around it made this a logical choice for me

### Spring Data Rest and application/hal+json

I did not use Spring Data Rest for automatic `hal+json` responses as I did not find it necessary for an application this size. 
Neither are there any relationships defined between the 2 main entities `Film` and `Customer`

### Testing

- An application that is longer than few lines of code should, in my opion, have tests 
- Most tests are integration tests using [RestAssured](http://rest-assured.io/), which I think is a great tool for integration testing as well as not having to manually test using Postman/curl/...
- Spring RestDocs are another great tool to keep your documentation up to date, while you barely need to write any documentation yourself

### Persistence

I've used an H2 in-memory database for this application. On a production system you'd be better of using something that actually persists your data :-) 

### Validation

I have spent most of my time on this part. A significant amount of time went into trying to have JSR-303 validation work. 
In the end I felt I was spending too much time on this though, which made me write my own validator. 
If you're stuck on something for too long, sometimes it might be better to try something else.
I have not chosen to spend too much time on validation messages, as you will see these could be more specific.

### Rest API design

- I feel like the structure of the request - and response bodies for the `/films/rent` and `/films/return` could be made more clear.
- I chose for a `POST` endpoint for the `/films/rent` endpoint even though a call to this endpoint doesn't change the state of the server or database. 
If not for the convenience of being able to use a request body, a `GET` mapping would've been more suitable.

### Dependency Injection

- I prefer constructor based injection, as this will guarantee an object will have be in a valid state after construction, as well as being easier to swap out dependencies for mocks.
I don't think this applies to injections within tests itself though, hence why I used field injection in test classes.
- I used the JSR-299 `@Named` and `@Inject` annotations instead of `@Component` and `@Autowired` as these are not tied to Spring

### Logging, monitoring, security, etc..

There are several aspects that a robust production application should have that I have not included in this demo application as those would have taken me too far
