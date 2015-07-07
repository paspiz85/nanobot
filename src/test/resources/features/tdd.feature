@unit-test
@wip
@tdd
Feature: scenarios for Test-Driven-Development


  Scenario Outline: check if rax is full
    Given screenshot saved as <imagefile>
    When checking if camps are full
    Then check is <check>

    Examples:
      | imagefile                                       | check |
      | classpath:/features/img/train_1435769837116.png | false |
      | classpath:/features/img/train_1435772811358.png | true  |
