Feature: Users can create new bank accounts

  Scenario Outline: User can create new accounts
    Given Account name <name>
    And Money balance <balance>
    When user create new account
    Then account is created
    And account id is available
    And balance is <balance>
    And account name is <name>

  Examples:
  | name | balance |
  | Foo  | 123.99  |

