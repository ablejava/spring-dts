



## 测试
1. 创建用户

curl -X POST "http://localhost:8080/customers?name=imooc1&password=123456"
curl -X GET http://localhost:8080/customers

curl -X PUT http://localhost:8080/customers/d11eab75-65ea-41dd-96b9-de4e04c631a1/deposit/1000


2. 创建票：

curl -X POST http://localhost:8080/tickets?name=t1

3. 创建订单

curl -X POST -d '{"customerId": "d11eab75-65ea-41dd-96b9-de4e04c631a1", "title": "order_1", "ticketId": "4c2a9e35-d711-428c-948e-0da499df0173", "amount": 100}' -H 'Content-Type: application/json' http://localhost:8080/orders