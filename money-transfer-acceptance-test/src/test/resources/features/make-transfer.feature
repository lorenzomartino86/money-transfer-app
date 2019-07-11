Feature: Users can make transfer from one account into another

  Scenario Outline: User can make a money transfer between accounts
    Given account <sourceAccount> with balance <sourceBalance>
    And account <targetAccount> with balance <targetBalance>
    When user makes transfer of <transfer> in currency <currency> with note <description>
    Then transfer is completed
    And source account has balance <sourceBalance> reduced by <transfer>
    And target account has balance <targetBalance> increased by <transfer>

  Examples:
  | sourceAccount | sourceBalance | targetAccount | targetBalance | transfer | currency | description |
  | Foo           | 123.99        | bar           | 22.01         | 51.99    | EUR      | Transfer 1  |
  | Weird Account | 20            | Lucky One     | 0.01          | 19.99    | EUR      | Transfer 2  |

  Scenario Outline: User cannot make a money transfer between accounts when balance is not enough
    Given account <sourceAccount> with balance <sourceBalance>
    And account <targetAccount> with balance <targetBalance>
    When user makes transfer of <transfer> in currency <currency> with note <description>
    Then transfer is rejected
    And source account has balance <sourceBalance> reduced by 0
    And target account has balance <targetBalance> increased by 0

  Examples:
  | sourceAccount | sourceBalance | targetAccount | targetBalance | transfer | currency | description |
  | Foo           | 123.99        | bar           | 22.01         | 1000.99  | EUR      | Transfer 1  |
  | Weird Account | 20            | Lucky One     | 0.01          | 20.01    | EUR      | Transfer 2  |