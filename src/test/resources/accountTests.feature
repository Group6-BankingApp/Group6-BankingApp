Feature: Retrieving accounts

  Scenario: Get accounts with valid parameters
    Given there are accounts in the system
    And I am logged in as a user
    When I request to get accounts with skip 0 and limit 10
    Then I should receive a list of accounts

  Scenario: Get accounts with invalid parameters
    Given there are accounts in the system
    And I am logged in as a user
    When I request to get accounts with skip -1 and limit 10
    Then I should receive a bad request response

