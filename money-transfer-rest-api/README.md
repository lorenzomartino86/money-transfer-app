# Rest Api Module
This module has the responsibility to expose and handle rest requests.

## API Design
Following RestFul resources are managed:
- Accounts
- Transfers

### Accounts APIs

- The following API will create a new account

    `POST http://localhost:8080/api/accounts`
       
   The requested fields are:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | name | Unique Account name | String |
    | balance | Account available balance | decimal |
    | currency | Account currency (ISO code) | String |    
    
   The response will be:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | id | Account unique identifier (UUID) | String |
    | name | Account name | String |
    | balance | Account available balance | decimal |
    | currency | Account currency (ISO code) | String |
    | createdAt | Creation date time (YYYY-MM-DD'T'hh:mm:ss) | String |
    
   * Sample request:
     
   `curl --header "Content-Type: application/json" --request POST --data '{"name":"Bar Account","balance":250.1,"currency": "GBP"}' http://localhost:8080/api/accounts`
  
        {
            "id": "3b4aa8de-30c4-4156-8129-4f20b2acb5e0",
            "name": "Bar Account",
            "balance": 250.01,
            "createdAt": "2019-07-11T10:24:06",
            "currency": "GBP"
        }


- The following API will return all accounts

        `GET http://localhost:8080/api/accounts`
    
    The response will be an array of:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | id | Account unique identifier (UUID) | String |
    | name | Account name | String |
    | balance | Account available balance | decimal |
    | currency | Account currency (ISO code) | String |
    | createdAt | Creation date time (YYYY-MM-DD'T'hh:mm:ss) | String |
    
    * Sample request:
     
         `curl -X GET http://localhost:8080/api/accounts`
          
            [
                {
                    "id": "b1b0f31a-a9e4-4f84-9b3d-d6ffa069ad2d",
                    "name": "Bar Account",
                    "balance": 250.01,
                    "createdAt": "2019-07-11T10:19:28",
                    "currency": "GBP"
                }
            ]

- The following API will return a single account by id

    `GET http://localhost:8080/api/accounts/<accountId>`
    
    The response will be an array of:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | id | Account unique identifier (UUID) | String |
    | name | Account name | String |
    | balance | Account available balance | decimal |
    | currency | Account currency (ISO code) | String |
    | createdAt | Creation date time (YYYY-MM-DD'T'hh:mm:ss) | String |

    * Sample request:
     
         `curl -X GET http://localhost:8080/api/accounts/b1b0f31a-a9e4-4f84-9b3d-d6ffa069ad2d`
          
            {
                "id": "b1b0f31a-a9e4-4f84-9b3d-d6ffa069ad2d",
                "name": "Bar Account",
                "balance": 250.01,
                "createdAt": "2019-07-11T10:19:28",
                "currency": "GBP"
            }
            
#### Transfers APIs      

- The following API will create a new transfer

    `POST http://localhost:8080/api/transfers`
       
   The requested fields are:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | fromAccountId | Source Account UUID | String |
    | toAccountId | Target Account UUID | String |
    | money | The money to transfer | Decimal |
    | currency | Account currency (ISO code). It should be the same currency of the source account. | String |    
    | description | Transfer notes | String |    
    
   The response will be:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | id | Transfer unique identifier (UUID) | String |
    | fromAccountId | Source Account UUID | String |
    | toAccountId | Target Account UUID | String |
    | money | The money to transfer | Decimal |
    | currency | Transfer currency (ISO code) | String |    
    | description | Transfer notes | String |  
    | createdAt | Creation date time (YYYY-MM-DD'T'hh:mm:ss) | String |
    
   * Sample request:
     
    `curl --header "Content-Type: application/json" \
            --request POST \
            --data '{"fromAccountId":"b1b0f31a-a9e4-4f84-9b3d-d6ffa069ad2d","toAccountId":"3b4aa8de-30c4-4156-8129-4f20b2acb5e0","money": 50.0 , "currency": "GBP", "description": "Test Bank Transfer"}' \
            http://localhost:8080/api/transfers`

          
        {
                "id": "bfaf519f-d208-4dab-919a-0df18da8ef56",
                "fromAccountId": "b1b0f31a-a9e4-4f84-9b3d-d6ffa069ad2d",
                "toAccountId": "3b4aa8de-30c4-4156-8129-4f20b2acb5e0",
                "description": "Test bank transfer",
                "money": 50,
                "currency": "GBP",
                "createdAt": "2019-07-11T10:35:11"
        }

 
- The following API will return all transfers for a specific account

    `GET http://localhost:8080/api/accounts/<accountId>/transfers
    
    The response will be an array of:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | id | Transfer unique identifier (UUID) | String |
    | type | Operation type: WITHDRAW or DEPOSIT | String |
    | money | Transfer money | decimal |
    | currency | Transfer currency (ISO code) | String |
    | description | Transfer notes | String |
    | createdAt | Creation date time (YYYY-MM-DD'T'hh:mm:ss) | String |

    * Sample request:
     
         `curl -X GET http://localhost:8080/api/accounts/b1b0f31a-a9e4-4f84-9b3d-d6ffa069ad2d/transfers`
          
            [
                {
                    "id": "bfaf519f-d208-4dab-919a-0df18da8ef56",
                    "type": "WITHDRAW",
                    "money": 50,
                    "currency": "GBP",
                    "description": "Test withdraw transfer",
                    "createdAt": "2019-07-11T10:35:11"
                },
                {
                    "id": "bfaf519f-d208-4dab-919a-0df18da8ef12",
                    "type": "DEPOSIT",
                    "money": 50,
                    "currency": "GBP",
                    "description": "Test deposit transfer",
                    "createdAt": "2019-07-12T10:35:11"
                }                
            ]
            
 
- The following API will return the transfer details by id

    `GET http://localhost:8080/api/transfers/<transferId>
    
    The response will be :
    
    | Field | Description | Type |
    | --- | --- | --- |
    | id | Transfer unique identifier (UUID) | String |
    | fromAccountId | Source Account UUID | String |
    | toAccountId | Target Account UUID | String |
    | money | The money to transfer | Decimal |
    | currency | Transfer currency (ISO code) | String |    
    | description | Transfer notes | String |  
    | createdAt | Creation date time (YYYY-MM-DD'T'hh:mm:ss) | String |

    * Sample request:
     
         `curl -X GET http://localhost:8080/api/transfers/bfaf519f-d208-4dab-919a-0df18da8ef56`
          
            {
                "id": "bfaf519f-d208-4dab-919a-0df18da8ef56",
                "fromAccountId": "b1b0f31a-a9e4-4f84-9b3d-d6ffa069ad2d",
                "toAccountId": "3b4aa8de-30c4-4156-8129-4f20b2acb5e0",
                "description": "Test bank transfer",
                "money": 50,
                "currency": "GBP",
                "createdAt": "2019-07-11T10:35:11"
            }