@unit-test
@wip
@tdd
@deprecated
Feature: scenarios for Test-Driven-Development


  Scenario Outline: search play game button
    Given screenshot saved as <imagefile>
    When searching play game button point
    Then point found at <coords>

    Examples:
      | imagefile                                           | coords    |
      | classpath:/features/img/base_1435356695457.png      | null      |
      | classpath:/features/img/screen_1440835521897.png    | [303,138] |
      