Feature: Users can create new bank accounts

  Scenario Outline: User can create new accounts
    Given name <name>
    And money balance <balance>
    And currency is <currency>
    When user create new account
    Then account is created
    And account id is available
    And balance is <balance>
    And account name is <name>
    And account currency is <currency>

  Examples:
  | name | balance  | currency |
  | Foo  | 123.99   | EUR      |
  | Bar  | 5000.98  | GBP      |

  Scenario Outline: User cannot create account with already used name
    Given account with name <name> balance <balance> and currency <currency>
    When user create account with name <name> balance <balance> and currency <currency>
    Then account creation is rejected with status code <status>

  Examples:
  | name     | balance  | currency | status |
  | Lorenzo  | 123.99   | EUR      | 409    |

  Scenario Outline: User cannot create account with not allowed currency code
    When user create account with name <name> balance <balance> and currency <currency>
    Then account creation is rejected with status code <status>

  Examples:
  | name     | balance  | currency | status |
  | Martino  | 123.99   | AUD      | 400    |