Feature: Users can make transfer from one account into another

  Scenario Outline: User can make a money transfer between accounts with same currency
    Given account <sourceAccount> with balance <sourceBalance> and currency <sourceCurrency>
    And account <targetAccount> with balance <targetBalance> and currency <targetCurrency>
    When user makes transfer of <transfer> in currency <currency> with note <description>
    Then transfer is completed
    And source account has balance <sourceBalance> reduced by <transfer>
    And target account has balance <targetBalance> increased by <transfer>

  Examples:
  | sourceAccount   | sourceBalance | sourceCurrency | targetAccount | targetBalance | targetCurrency | transfer | currency | description |
  | Mickey Mouse    | 123.99        | EUR            | Goofy         | 22.01         | EUR            | 51.99    | EUR      | Transfer 1  |
  | Duffy Duck      | 20            | EUR            | Bugs Bunny    | 0.01          | EUR            | 19.99    | EUR      | Transfer 2  |

  Scenario Outline: User can make a money transfer between accounts with different currencies
    Given account <sourceAccount> with balance <sourceBalance> and currency <sourceCurrency>
    And account <targetAccount> with balance <targetBalance> and currency <targetCurrency>
    When user makes transfer of <transfer> in currency <currency> with note <description>
    Then transfer is completed
    And source account has balance <sourceBalance> reduced by <transfer>
    And target account has balance <targetBalance> increased by <convertedTransfer>

  Examples:
  | sourceAccount    | sourceBalance | sourceCurrency | targetAccount  | targetBalance | targetCurrency | transfer | convertedTransfer | currency | description |
  | Mickey Mouse EUR | 100.0         | EUR            | Goofy USD      | 10.0          | USD            | 10.0     | 11.21             | EUR      | Transfer 1  |
  | Duffy Duck EUR   | 30            | USD            | Bugs Bunny GBP | 10.0          | EUR            | 10.0     | 8.93              | USD      | Transfer 2  |

  Scenario Outline: User cannot make a money transfer between accounts when balance is not enough
    Given account <sourceAccount> with balance <sourceBalance> and currency EUR
    And account <targetAccount> with balance <targetBalance> and currency EUR
    When user makes transfer of <transfer> in currency <currency> with note <description>
    Then transfer is rejected
    And source account has balance <sourceBalance> reduced by 0
    And target account has balance <targetBalance> increased by 0

  Examples:
  | sourceAccount | sourceBalance | targetAccount | targetBalance | transfer | currency | description |
  | Donald Duck   | 123.99        | Daisy Duck    | 22.01         | 1000.99  | EUR      | Transfer 1  |
  | Rick Grimes   | 20            | Shane         | 0.01          | 20.01    | EUR      | Transfer 2  |