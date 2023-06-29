Feature: User Management

  Scenario: User login
    Given the endpoint for "/login" is available for method "POST"
    When I make a login request with valid credentials
    Then the response should contain a token

  Scenario: Get all users with associated accounts
    Given the endpoint for "/withAccount" is available for method "GET"
    And I make a login request with valid credentials
    When I request to get all users with associated accounts
    Then the response should contain a list of users with their account details

#  Scenario: Get all users without associated accounts
#    Given the endpoint for "/withoutAccount" is available for method "GET"
#    When I request to get all users without associated accounts
#    Then the response should contain a list of users without any account details

#  @admin
#  Scenario: Get all users (admin access)
#    Given the endpoint for "/" is available for method "GET"
#    And I have admin access
#    When I request to get all users
#    Then the response should contain a list of all users

#  Scenario: Get user by ID
#    Given the endpoint for "/{id}" is available for method "GET"
#    When I request to get the user with ID "{id}"
#    Then the response should contain the user details for ID "{id}"

#  Scenario: Add a new user
#    Given the endpoint for "/" is available for method "POST"
#    And I have the necessary user details
#    When I add a new user
#    Then the response should contain the newly created user details

#  Scenario: Update an existing user
#    Given the endpoint for "/{id}" is available for method "PUT"
#    And I have the necessary user details for ID "{id}"
#    When I update the user with ID "{id}"
#    Then the response should contain the updated user details

#  Scenario: Delete a user
#    Given the endpoint for "/{id}" is available for method "DELETE"
#    When I delete the user with ID "{id}"
#    Then the user with ID "{id}" should be deleted