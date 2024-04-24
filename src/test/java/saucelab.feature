Feature: SauceLabTest

  Scenario: verify user can login successfully
    Given user open the saucelab login page
    When user login with valid credential
    Then user see saucelab homepage

  Scenario: verify user can sorting the price from high to low
    Given user open the saucelab login page
    When user login with valid credential
    And user sorting the price high to low
    Then user see the price sorted from high to low