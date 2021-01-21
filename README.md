leipzig-tech-ba
===============

A repository for the backend of leipzigtech.de.

postgres for development
------------------------
* build image with `docker build -t leipzigtech .`
* adjust the path for the volume in docker-compose.yml for your environment
* `docker-compose up -d` for starting the postgres on port 5432


Running backend spring boot
------------------------

* `gradle build`

* `Run 'BaApplication'`

Starting backend on port 8080

Testing with DB
------------------------


#### Connecting with DB


Make sure that you have free ports at 5432 (or 8080 for connecting with backend on localhost).
Registered ssh public key on server required.


* ssh -L 5432:localhost:5432 root@46.101.174.176



#### API
* GET /companies/:id -> Returns a specific company with a given id

* POST /companies -> Creates a new company based on a given JSON payload
```
      "ref":"134-ddssweeee-sss",
      "source":"gelbeSeiten",
      "name": "Microsoft",
      "sector":"Tech",
      "city":"Leipzig",
      "plz":"04155",
      "address":"Georg-Schumann-Straße 100",
      "phonenumber":"03416666666",
      "website":"microsoft.com",
      "email":"apple@kaufmich.com"
```

* DELETE /companies/:id -> Delete a specific company with a given id
------------------------

* GET /companies -> Returns all companies

The following query parameter should be supported by the upper endpoints:
* name -> Name of the company / String
* fuzzy -> Enable fuzzy search / Boolean (default: true)
* orderBy -> sorts the records by "keyword" (default:name)
* sort -> Order of results / Enum with Values ASC (ascending) and DESC (descending) (default: ASC)


GET /companies?name=Microsof&fuzzy=true&sort=DESC 
GET /companies?orderBy=create_at&fuzzy=true&sort=ASC





