@unit-test
Feature: parsing main screen

  Scenario Outline: parse attack button
    Given screenshot saved as <imagefile>
    When parsing attack button
    Then attack button is <found>

    Examples:
      | imagefile                                      | found |
      | classpath:/features/img/main_1435575631326.png | false |
      | classpath:/features/img/main_1435601238653.png | true  |
