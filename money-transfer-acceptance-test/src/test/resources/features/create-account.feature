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

