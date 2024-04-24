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

  Scenario: verify user successful purchase for 2 items which has $15.99 price.
    Given user open the saucelab login page
    When user login with valid credential
    And user purchase 2 item which has "$15.99" price
    And user fill the buyer details
    Then user successful complete purchase items

  Scenario: verify user check the Log checkout summary.
    Given user open the saucelab login page
    When user login with valid credential
    And user purchase 2 item which has "$15.99" price
    And user fill the buyer details
    Then user see correct details of checkout summary

  Scenario: Login with locked_out_user account
    Given user open the saucelab login page
    When user login with "locked_out_user" username and "secret_sauce" password
    Then user should see error message indicating the user is locked out
