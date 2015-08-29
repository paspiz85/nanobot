@unit-test
Feature: parsing attack screen

  Scenario Outline: search end battle button
    Given screenshot saved as <imagefile>
    When searching end battle return home button point
    Then point found at <coords>

    Examples:
      | imagefile                                           | coords    |
      | classpath:/features/img/base_1435356695457.png      | null      |
      | classpath:/features/img/endbattle_1438533159747.png | [437,532] |
