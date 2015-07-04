@unit-test
@wip
Feature: test work-in-progress

    Scenario Outline: parse troops
    Given screenshot saved as <imagefile>
    When parsing troops
    Then troops count is <troops_count>

    Examples:
      | imagefile                                      | troops_count   |
      | classpath:/features/img/base_1435906242449.png | [64,74,36,1,0] |
 