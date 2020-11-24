leipzig-tech-ba
===============

A repository for the backend of leipzigtech.de.

postgres for development
------------------------
* adjust the path for the volume in docker-compose.yml for your environment
* `docker-compose up -d` for starting the postgres on port 5432


Running backend spring boot
------------------------

* `gradle build`

* `Run 'BaApplication'`

Starting backend on port 8080

Manual testing
------------------------

* http://localhost:8080/companies

* http://localhost:8080/locations
