# Notes about spring cloud gateway zuul

## Routes and prefix

- if we use `strip-prefix=true` (default config), with config:

```yaml

zuul:
  prefix: /api
  routes:
    account:
      path: /customer-api/**
      serviceId: customer-service
    order:
      path: /order-api/**
      serviceId: order-service
      strip-prefix: true
```

then when we call url `http://localhost:8060/api/customer-api/v1/customers?id=1` from client, api gateway will receive it and route it by call to customer service in url: `http://localhost:8071/v1/customers?id=1`

- if we use `strip-prefix=false`, with config:

```yaml

zuul:
  prefix: /api
  routes:
    account:
      path: /customer-api/**
      serviceId: customer-service
      strip-prefix: false
    order:
      path: /order-api/**
      serviceId: order-service
      strip-prefix: false
```

then when we call url `http://localhost:8060/api/customer-api/v1/customers?id=1` from client, api gateway will receive it and route it by call to customer service in url: `http://localhost:8071/customer-api/v1/customers?id=1`

