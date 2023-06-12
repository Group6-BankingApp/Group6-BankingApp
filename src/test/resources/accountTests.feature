Feature: Retrieving accounts

  Scenario: Get accounts with valid parameters
    Given there are accounts in the system
    When I request to get accounts with skip 0 and limit 10
    Then I should receive a list of accounts with size 10

  Scenario: Get accounts with invalid parameters
    When I request to get accounts with skip -1 and limit 10
    Then I should receive a bad request response

