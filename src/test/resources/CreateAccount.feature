Feature: Create Account

#  Scenario: Create account with valid data
#    Given I am logged in as an account admin
#    When I provide valid account data
#    Then the response should contain the created account details and the status code should be 201

  Scenario: Create account with invalid data
    Given I am logged in as an account admin
    When I provide invalid account data
    Then the account creation should fail with error code 400
