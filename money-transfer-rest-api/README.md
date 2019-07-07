
## API First Design

### Create Account

*Request*
 
 POST http://localhost:8080/api/1.0/accounts

{
    "name": "Foo Account",
    "balance": 100.00
}

*Response*

  {
    "id": "2a0d4d03-e26c-4159-9de1-c6bf3adfd8a1",
    "name": "Foo Account",
    "balance": 100.00,
    "created_at": "2017-06-01T11:11:11.1Z"
  }

### Get Accounts

*Request*
 
 GET http://localhost:8080/api/1.0/accounts

*Response*

[
  {
    "id": "2a0d4d03-e26c-4159-9de1-c6bf3adfd8a1",
    "name": "Foo Account",
    "balance": 100.00,
    "created_at": "2017-06-01T11:11:11.1Z"
  },
  {
    "id": "df8d6b20-0725-482e-a29e-fb09631480cf",
    "name": "Bar Account",
    "balance": 1234.00,
    "created_at": "2017-06-01T11:11:11.1Z"
  }
]

### Get Account by ID


*Request*
 
 GET http://localhost:8080/api/1.0/accounts/<Account ID>

*Response*


  {
    "id": "2a0d4d03-e26c-4159-9de1-c6bf3adfd8a1",
    "name": "Foo Account",
    "balance": 100.00,
    "created_at": "2017-06-01T11:11:11.1Z"
  }



### Create transfer


*Request*
 
 POST http://localhost:8080/api/1.0/transfers
 
 With body request
 
 {
   "from_account_id": "bdab1c20-8d8c-430d-b967-87ac01af060c",
   "to_account_id": "5138z40d1-05bb-49c0-b130-75e8cf2f7693",
   "amount": 123.11,
   "description": "Expenses funding"
 }

*Response*

{
  "id": "62b61a4f-fb09-4e87-b0ab-b66c85f5485c",
  "state": "completed",
  "created_at": "2017-06-21T11:22:11.1Z",
  "completed_at": "2017-06-21T11:22:11.1Z"
}
