@unit-test
@wip
@tdd
Feature: scenarios for Test-Driven-Development


  Scenario Outline: parse troops
    Given screenshot saved as <imagefile>
    When counting troops
    Then troops count is <troops_count>

    Examples:
      | imagefile                                         | troops_count |
      | classpath:/features/img/screen_1436215222084.png  | [53,100,42]  |
