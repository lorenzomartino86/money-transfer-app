Feature: Users  can make transfer from one account into another

  Scenario Outline: User can make a money transfer between accounts
    Given account <account1> with balance <balance1>
    And account <account2> with balance <balance2>
    When user makes transfer of <transfer> between <account1> and <account2> with note <description>
    Then transfer is completed
    And account <account1> has balance <balance1> reduced by <transfer>
    And account <account2> has balance <balance2> increased by <transfer>

  Examples:
  | account1 | balance1 | account2 | balance2 | transfer | description |
  | Foo      | 123.99   | bar      | 22.01    | 51.99    | Transfer 1  |

