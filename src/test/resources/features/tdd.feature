@unit-test
@wip
@tdd
Feature: scenarios for Test-Driven-Development


  Scenario Outline: search full gold mine
    Given screenshot saved as <imagefile>
    When searching full gold mine points
    Then points found are <pointset>

    Examples:
      | imagefile                                      | pointset    |
      | classpath:/features/img/main_1435696574573.png | [449,436] |
      | classpath:/features/img/main_1435698446657.png | [261,225] |
      | classpath:/features/img/main_1435699320137.png | [618,281] |
      | classpath:/features/img/main_1435700312519.png | [440,162] |
      | classpath:/features/img/main_1435701087138.png | [308,401] |


