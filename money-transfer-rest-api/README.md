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
    | currency | Account currency (ISO code) - Allowed codes are EUR, USD, GBP | String |    
    
   The response will be:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | id | Account unique identifier (UUID) | String |
    | name | Account name | String |
    | balance | Account available balance | decimal |
    | currency | Account currency (ISO code) | String |
    | createdAt | Creation date time (YYYY-MM-DD'T'hh:mm:ss) | String |


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

 
- The following API will return all transfers for a specific account

    `GET http://localhost:8080/api/accounts/<accountId>/transfers
    
    The response will be an array of:
    
    | Field | Description | Type |
    | --- | --- | --- |
    | id | Transfer unique identifier (UUID) | String |
    | type | Operation type: WITHDRAW or DEPOSIT | String |
    | amount | Transfer money | decimal |
    | currency | Transfer currency (ISO code) | String |
    | description | Transfer notes | String |
    | createdAt | Creation date time (YYYY-MM-DD'T'hh:mm:ss) | String |
 