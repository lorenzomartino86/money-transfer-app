# Domain Module
This module has the responsibility to handle business logic of money transfers between accounts domain.

Technical Details:
1. The most lightweight module and it doesn't use any framework. Just *lombok* to write less boilerplate code.
2. Unit tests run in isolation using *Junit* and *EasyMock*.

## Domain Model
Following diagram resumes the adopted domain model.
![alt text](design/domain-design.png)

Model is mapped in three different groups:

1. **Use Cases**: Services classes responsible to carry on the business request.
2. **Domain Models**: Group of entities and value objects.
3. **Repositories**: The abstract repositories required by the business model.  
