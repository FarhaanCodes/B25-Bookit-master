@smoke
Feature: User Verification


  Scenario: verify information about logged user
    Given I logged Bookit api using "fscoughx@msu.edu" and "feodorascough"
    When I get the current user information from api
    Then status code should be 200

  @db @wip
  Scenario: verify information about logged user from api and database
    Given I logged Bookit api using "fscoughx@msu.edu" and "feodorascough"
    When I get the current user information from api
    Then the information about current user from api and database should match
