# Repository Module

This module has the responsibility to handle repository implementations for CRUD operation on the backing database.

Technical Details:
1. Usage of Java lightweight orm **ormlite**
2. H2 in memory database
3. Three persistence entities: 
  - Account
  - Transfer
  - ExchangeRate
  
This module is covered by integration tests running faster due to in memory database implementation.