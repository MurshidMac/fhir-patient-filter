### Demo project for libreheath gsoc 2018

## Filter patients from FHIR documents.

#### Start the app

##### For MongoDB
- run this in the root of the project
```
docker-compose up
```
This will fire up an instance of mongodb with **host:** `mongo` and **database** `test` with no authentication on **port** `27017`  
- you can close this instance by Pressing `Ctrl-C` in the terminal it was open

##### For UI
 - `cd` to `frontend` and run
```
npm install
npm start
```

##### For Server
- `cd` to `server` and run
```
mvn spring-boot:run
```

Finally access the project by navigating to *localhost:4200* from your browser.

__Note:__

* To change mongodb settings, specify them in the `application.properties` file. For e.g.
```
spring.data.mongodb.port=27018
spring.data.mongodb.host=mongoserver
spring.data.mongodb.username=test
spring.data.mongodb.password=testpass
```
etc.
* To change server port, you need to modify 2 files,
  * `server.port` in `server/src/main/resources/application.properties`
  * `target` in `frontend/proxy.conf.json`
* To change FHIR server address, modify `fhir.server.url` in  
`server/src/main/resources/application.properties`
