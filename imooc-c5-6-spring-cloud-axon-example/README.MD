



## 测试
1. 创建用户

curl -X POST "http://localhost:8888/user/customers?name=imooc1&password=123456"
curl -X GET http://localhost:8888/user/customers

curl -X PUT http://localhost:8888/user/customers/fc47c0c2-10b3-4aff-9f90-cd00a728b65c/deposit/1000


2. 创建票：

curl -X POST http://localhost:8888/ticket/tickets?name=t1

3. 创建订单

curl -X POST -d '{"customerId": "fc47c0c2-10b3-4aff-9f90-cd00a728b65c", "title": "order_1", "ticketId": "f87d791f-96eb-4859-93d4-9a179c409f81", "amount": 100}' -H 'Content-Type: application/json' http://localhost:8888/order/orders