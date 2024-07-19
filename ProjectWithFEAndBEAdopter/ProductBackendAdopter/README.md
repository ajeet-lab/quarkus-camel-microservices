# PRODUCTBACKENDADOPTER

### API 1:
```
curl --location 'http://localhost:9902/api/v1/backendadopter/products'
```

### API 2:
```
curl --location 'http://localhost:9902/api/v1/backendadopter/product/search' \
--header 'Content-Type: application/json' \
--data '{
    "title":"Laptop"
}'
```

### API 3:
```
curl --location 'http://localhost:9902/api/v1/backendadopter/product/limitskipandselectedfield' \
--header 'Content-Type: application/json' \
--data '{
    "limit": 5,
    "skip": 10,
    "select": "title,price"
}'
```