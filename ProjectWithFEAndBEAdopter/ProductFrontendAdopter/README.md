# PRODUCTFRONTENDADOPTER

### API 1:
```
curl --location 'http://localhost:9092/api/v1/frontend/products' \
--header 'Content-Type: application/json' \
--header 'SourceName: BACKENDADOPTER' \
--header 'Authorization: abc' \
--header 'ConversationID: 1234567890'
```

### API 2:
```
curl --location 'http://localhost:9092/api/v1/frontend/product/search' \
--header 'Content-Type: application/json' \
--header 'SourceName: BACKENDADOPTER' \
--header 'Authorization: abc' \
--header 'ConversationID: 1234567890' \
--data '{
    "title":"Laptop"
}'
```

### API 3:
```
curl --location 'http://localhost:9092/api/v1/frontend/product/limitskipandselectedfield' \
--header 'Content-Type: application/json' \
--header 'SourceName: BACKENDADOPTER' \
--header 'Authorization: abc' \
--header 'ConversationID: 1234567890' \
--data '{
    "limit": 2,
    "skip": 10,
    "select": "title,price"
}'
```