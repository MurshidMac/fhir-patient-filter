### Demo project for libreheath gsoc 2018

## Filter patients from FHIR documents.

#### Start the app

##### For UI
 - `cd` to `frontend` and run
```
npm start
```

##### For Server
- `cd` to `server` and run
```
mvn spring-boot:run
```

__Note:__

* To change server port, you need to modify 2 files,
  * `server.port` in `server/src/main/resources/application.properties`
  * `target` in `frontend/proxy.conf.json`
* To change FHIR server address, modify `fhir.server.url` in  
`server/src/main/resources/application.properties`
* To access a minimal GUI for database,
  * run the server and navigate to  
`localhost:<port>/h2-console`.
  * Enter `jdbc:h2:mem:testdb` in `JDBC URL` box and click on _connect_.
