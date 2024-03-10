<h1>Log Puller Service</h1>

API based log searching

<h3>How to Run the Application</h3>
ACCESS_KEY and SECRET_KEY need to be provided for running the application in application-prod.yml.
In application.yml: change `spring.profiles.active: dev` to `spring.profiles.active: prod`

<h3>Assumptions:</h3>
<p>app-logs-bucket is the bucket name for folder structure in S3</p>
<p>Search keyword is always present in the request</p>
<p>From and to timestamps might not always be present in the request</p>
<p>If both are present in the request: from and to range would be considered else if from is present, from till the whole length; if to is present starting till the to length; if none are present, all files are considered</p>

<h3>Curl</h3>
`curl --location --request GET 'localhost:8080/v1/grep/search' \
--header 'Content-Type: application/json' \
--data '{
"searchKeyword": "hello"
}'`

`curl --location --request GET 'localhost:8080/v1/grep/search' \
--header 'Content-Type: application/json' \
--data '{
"searchKeyword": "hello",
"from": "2024-01-10T20:52:42.345060Z",
"to": "2024-03-10T20:52:42.345060Z"
}'
`