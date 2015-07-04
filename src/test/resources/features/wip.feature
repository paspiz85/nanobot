@unit-test
@wip
Feature: test work-in-progress


  Scenario Outline: search troops button
    Given screenshot saved as <imagefile>
    When searching troops button point
    Then point found at <coords>

    Examples:
      | imagefile                                       | coords    |
      | classpath:/features/img/main_1436034229185.png  | [63,1160] |


  Scenario Outline: search close troops button
    Given screenshot saved as <imagefile>
    When searching close troops button point
    Then point found at <coords>

    Examples:
      | imagefile                                       | coords    |
      | classpath:/features/img/main_1436034229185.png  | null      |
      | classpath:/features/img/train_1435772811358.png | [756,749] |
      