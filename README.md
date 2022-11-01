# synpulse8-apps
[CircleCI Link](https://app.circleci.com/pipelines/github/hohang210?filter=all) | 
[Docker Image](https://hub.docker.com/repository/docker/hohang210/synpulse8) | 
[Kubernetes Config]()

This repository contains an e-banking portal implemented by Spring boot, Spring MVC, Spring security, Mybatis.  
This e-banking portal provides APIs for authenticated and authorized user to create bank account 
and check their transactions under their owned accounts.
See the document belows for more details.

## Run Locally
This e-banking portal requires couples of server, includes mysql, redis, zookeeper and kafka.  

To run it on your local, please follow the followed steps:

- Install docker on your local machine. Refer to: https://www.docker.com/
- Create docker network:
  - Run `docker network create synpulse8`
- Create a mysql server
  - Run `docker pull mysql:5.7` to pull a mysql image
  - Run `docker run -id -p 3306:3306 --network=synpulse8 --name=mysql -e MYSQL_ROOT_PASSWORD=your_password mysql:5.7` to create and run mysql container
- Create a redis server
  - Run `docker pull redis:5.0` to pull a redis image
  - Run `docker run -id --network=synpulse8 --name=redis -p 6379:6379 redis:5.0` to create and run redis container
- Create a zookeeper server
  - Run `docker pull wurstmeister/zookeeper:latest` to pull a zookeeper image
  - Run `docker run -d --network=synpulse8 --name zookeeper -p 2181:2181 -e ZOOKEEPER_CLIENT_PORT=2181 wurstmeister/zookeeper` to create an run redis container
- Create a kafka server
  - Run `docker pull wurstmeister/kafka:latest` to pull a kafka image
  - Run `docker run -d --network=synpulse8 --name kafka -p 9092:9092 -e KAFKA_ADVERTISED_HOST_NAME=kafka -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 wurstmeister/kafka` to create and run kafka container
- Clone the repo and setup env variable
  - export MYSQL_HOST=localhost
  - export MYSQL_PORT=3306
  - export MYSQL_PASSWORD=your_password
  - export MYSQL_USERNAME=root
  - export REDIS_HOST=localhost
  - export REDIS_PORT=6379
  - export KAFKA_HOST=localhost
  - export KAFKA_PORT=9092
- Now you can run the application.

## Architecture
The followed picture described how the servers communicates between each other.

![Architecture.png](https://github.com/hohang210/synpulse8-apps/blob/master/images/Architecture.png)

Currently, API Gateway server and Account backend are still in one server, but in different packages.

In the next version, will use spring-cloud to separate API Gateway server and Account Backend server into
two different servers.

Only Api Gateway will be exposed to public, all other servers will stand behind the API Gateway.  

All the requests have to go through API Gateway, and the API Gateway will authenticate and authorize all users for different requests.

Account Backend listens to a kafka server, all transactions consumed from kafka will be saved into mysql server.

Both API Gateway and Account backend will use a common mvn package which contains shared utilities and RBAC model.

## User/Security Model
### Introduction
The content on the e-banking portal app is managed by users and those users are going to have different levels of access.
Introduce RBAC model to handle access.

### Definition
#### Users
A user is a real person who logs into the system using their own username and password.
#### Roles
Roles are simply a user-friendly name applied to a permission set in current e-banking portal.  E.g. A 
user will have a name like 'username User Role', but would grant a set of resources applied to some users 
that can access different resources.
#### SystemMenus
A systemMenu set is the resources that users can perform 'actions'.  For example, a user applied with resource '/account/iban' 
will have permission to check all the info under the given iban account.
#### Actions
Actions are the various things users can do such as createAccount, getTransaction and so on.

### Managing Users, Roles and SystemMenus
Users can have zero or more assigned roles and each role will have zero or more systemMenus
### Defined Resources and Actions
Here is brief list of resources in the system.
- /account for creating account or other basic account operation.
- /account/iban for creating transactions or retrieving any info under the given iban.

### Implementation
![RBAC.png](https://github.com/hohang210/synpulse8-apps/blob/master/images/RBAC.png)
- `User` object have some properties defined such as id, username, password and so on.
- `UserRole` object is used to determined what kind of roles a user has.
- `Role` object has a user-friendly name and applied to a set of `SystemMenu` objects by `RoleMenu` object.
- `SystemMenu` object will indicate whether the permission is being granted or denied and will define the scope of permission.
- LoginUser object will receive a user object and retrieve all the permission of the given user.

### Authentication
![AuthenticationFilters.png](https://github.com/hohang210/synpulse8-apps/blob/master/images/AuthenticationFilters.png)
- First of all, every request will go through the `JWTUsernamePasswordAuthenticationFilter` filter first.  
  - This filter will retrieve a JWT from the header with attribute `Authorization`.  
  - Next step, this filter will validate the JWT and retrieve an authenticated user from redis based on the JWT.  Will throw out a runtime exception if JWT 
    is not valid. 
  - Finally, this will filter will put the authenticated user with its granted resources into a context for the usage of next filter.
- Secondly, requests from un-logged-in user(JWT is not retrieved in previous filter)
  will go through the `JWTUsernamePasswordAuthenticationFilter`.
  - First of all, this filter will fetch `username` and `password` from the request body.
  - Secondly, this filter will attempt to log in and compare the provided password with the password saved in database 
    if `username` and `password` are defined in request body,
  - In addition, this filter will send back a valid JWT back to the client and saved the JWT and logged-in user to redis. if the provided password matches the password saved in db.
  - Lastly, this will filter will put the authenticated user with its granted resources into a context.

### Authorization
During the process of authentication, authenticated user is stored in a context with its granted resources.  The 
annotation `@PreAuthorize` of Spring Security will handle the authorization of each user's requests/actions. For example, 
controllers with `@PreAuthorize("hasAuthority('/account/' + #iban)")` can be only accessed by user who with granted resource `/account/iban`

## Account Operations
### Introduction
E-banking portal provides a couple of APIs for user to operate an account, such as create a new account and 
retrieve account's transactions.

### Definition
#### Accounts
A banking account that a user own.  Each user can have one or more account is different currencies(e.g. CAD, EUF).
#### Transactions
Transactions are always happened between account and account.  One account can transfer a specified amount to another account.

### APIs
E-banking provides some APIS for users to create account and fetch transactions.  For more details, please start the application
and check on `/swagger-ui.html`

#### signUp
path: `/signUp`

Creates a new user and associated the user with some basic roles, such as creating account and so on.

#### login
path: `/login`

Provides a log in API for user to log in.  Stores all logged-in users info to redis.

#### createAccount
path: `/account/createAccount`

Attempts to create an account for the logged-in user.  The logged-in user will have a new granted permission to access
this account if account is created successfully.

#### createTransaction
path: `/account/createTransaction/{iban}`

Attempts to create a transaction under given account and sends the transaction to kafka.
The newly-created transaction will not be saved to db directly.  There will be a listener consumes the transaction and saves the transaction into db.

NOTE: This API does not support to transfer money from one account to another account.  This API is only used for
creating a fake transaction for testing usage.

#### getTransactionsByIban
path: `/account/getTransactions/{iban}`

Attempts to retrieve a list of transactions by its account iban.
Returns a Page result with the total number of transactions and the total credit and debit value of the current page
transactions.

#### logout
path: `/logout`

Provides a log out API for user to log out.  Deletes the logged-out user's info from redis.


### Implementation
![AccountOperations.png](images/AccountOperations.png)
- `Account` have some properties defined such as id, iban and currency.  And iban is a unique account 
   number for the usage of transferring money between different accounts.
- `UserAccount` object is used to link the `User` object and `Account` object.  Each user can have one or more accounts.
- `AccountManager` object is used to do database check while creating a new account and manage the relationship 
   between account and user.
- `AccountService` is used to handle the business logics, such as assigning the newly-created account to the user, applying granted resource to the 
  user for the newly-created account while creating a new account under a specified logged-in user.
- `AccountTransactionManager` object is similar to `AccountManager`, which is used to do database check while creating transaction 
   creation and retrieving transactions.
- `AccountTransactionService` provides functions to deal with business logics while retrieving transactions.
- `AccountController` provides APIs for logged-in user to do account operations, such as creating account or getting account's transactions.
- `Page` object is used to wrap up a set of data retrieved from database.  Describes the total number of data and some additional information.
- `ResponseResult` object is a response data of all APIs which describes the result and status of a request's result.

### Manage Transactions
All transaction cover the last ten years and are stored in Kafka with the key being the transaction 
ID and the value hte JSON representation of the transaction.  

A Kafka listener is set up in account backend to receive a batch of transaction JSONs and start a new thread to 
cast it to current `Transaction` model.  The listener will commit the offset immediately and receive the next batch of transactions
after creating the thread. All the attributes from a transaction JSON will be ignored if it is not matched to current 
`Transaction` model.  A logger will log out any transaction JSONs and their Kafka offsets if they fail to cast to 
`Transaction` model.  Then, all transactions will be saved in to database using a mechanism similar to Hashmap 
in current version.  A specified transaction will be saved into table based on the hash value of transactionId 
divided by 5000.  Since transaction id is a unique key of a transaction, a transaction will not be saved in database twice.  
In the next version, will use `Sharding JDBC` to manage the transaction data.

### Audit
Every account operations from all users will be recorded.  For example, the system will record a message 
"User - 1 is trying to request resource http://localhost:8889/account/createAccount" when they are trying to create
a new banking account.  In current implementation, the system just simply logged out a message.  In the next version, 
will produce a set of messages data to Kafka and these these messages data will be consumed in a monitoring platform.

