# seth-tips
[![Build Status](https://travis-ci.org/Erechtheus/seth-tips.svg?branch=master)](https://travis-ci.org/Erechtheus/seth-tips)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Project description
Seth-tips is an annotation service according to the BioCreative V.5. BeCalm task [TIPS](http://www.becalm.eu/files/material/BioCreative.V.5_CFP.pdf).
Annotations for mutation mentions are generated using [SETH](https://github.com/rockt/SETH), [mirNer](https://github.com/Erechtheus/mirNer), and diseases using a dictionary lookup. Results are returned in JSON according to these  [definitions](http://www.becalm.eu/files/schemas/jsonSchema.json). 


## Getting Started

> #### Note
> The system uses RabbitMQ to load balance, so make sure it is running locally before starting the application, refer to [how to install RabbitMQ](https://www.rabbitmq.com/download.html) for help.

To start the system in development mode issue

    ./mvnw spring-boot:run

This starts the backend without submitting results to the tips server, instead results are printed to the console.
The server is listening on port `8080` per default.

### getAnnotation

Issue the following `curl` request to trigger a new annotation request

    curl -vX POST http://localhost:8080/call -d @src/test/resources/samplepayloadGetannotations.json --header "Content-Type: application/json"

### getStatus

To trigger a get status report, use the following `curl` request

    curl -vX POST http://localhost:8080/call -d @src/test/resources/sampleplayloadGetStatus.json --header "Content-Type: application/json"

#### close hanging requests

    curl -H "Content-Type: application/json" -X POST -d  '' http://www.becalm.eu/api/saveAnnotations/JSON?apikey={apikey}&communicationId={communicationId}
