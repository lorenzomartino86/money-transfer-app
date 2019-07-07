Feature: Users can create account

  Scenario Outline: User can create new accounts
    Given Account name <name>
    And Money balance <balance>
    When user create new account
    Then account is created
    And account id is available

  Examples:
  | name | balance |
  | Foo  | 123.99  |

