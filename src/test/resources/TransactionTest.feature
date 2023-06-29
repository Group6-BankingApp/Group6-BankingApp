Feature: Transactions CRUD operations

  Scenario: Getting all transactions
    Given The endpoint for "transactions" is available for method "GET"
    And I am logged in as an admin
    When I retrieve all transactions
    Then I should receive all transactions

  #Scenario: Create a transaction
  #  Given The endpoint for "transactions" is available for method "POST"
  #  When I create a transactions with senderIban "NL09INGB87654321" receiverIban "cash" with amount 50 and description "withdraw"
  #  Then The response status is 201
  #  And The timeCreated should be set to the current time