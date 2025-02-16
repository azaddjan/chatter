# chatter


curl --location 'localhost:8800/api/chat' \
--header 'Content-Type: application/json' \
--data '{
"question": "How is weather in New York City?"
}'

curl --location 'localhost:8800/api/complex' \
--header 'Content-Type: application/json' \
--data '{
"question": "How is weather in New York City?"
}'


localhost:8800/actuator/metrics/gen_ai.client.token.usage
localhost:8800/actuator/metrics/jvm.buffer.memory.used

http://localhost:9090/query
