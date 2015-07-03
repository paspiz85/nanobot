@unit-test
Feature: parsing attack screen

  Scenario Outline: parse enemy info
    Given screenshot saved as <imagefile>
    When parsing enemy info
    Then enemy info found is <gold>, <elixir>, <dark_elixir>, <thophy_win>, <thophy_defeat>

    Examples:
      | imagefile                                      | gold   | elixir | dark_elixir | thophy_win | thophy_defeat |


  Scenario Outline: check collectors
    Given screenshot saved as <imagefile>
    When checking collectors
    Then collectors is <full>

    Examples:
      | imagefile                                      | full  |
      | classpath:/features/img/base_1435356695457.png | false |
      | classpath:/features/img/base_1435356702569.png | false |
      | classpath:/features/img/base_1435356708541.png | false |
      | classpath:/features/img/base_1435356717499.png | false |
      | classpath:/features/img/base_1435356721666.png | true  |
      | classpath:/features/img/base_1435356725776.png | false |
      | classpath:/features/img/base_1435356729468.png | true  |
      | classpath:/features/img/base_1435358337068.png | false |
      | classpath:/features/img/base_1435358344374.png | false |
      | classpath:/features/img/base_1435358350820.png | false |
      | classpath:/features/img/base_1435358355546.png | true  |


  Scenario Outline: parse troops
    Given screenshot saved as <imagefile>
    When parsing troops
    Then troops count is <troops_count>

    Examples:
      | imagefile                                      | troops_count |
      | classpath:/features/img/base_1435356695457.png | [81,65,54,0] |


  Scenario Outline: search next button
    Given screenshot saved as <imagefile>
    When searching next button point
    Then point found at <coords>

    Examples:
      | imagefile                                      | coords    |
      | classpath:/features/img/base_1435356695457.png | null      |
      | classpath:/features/img/base_1435775196561.png | [714,516] |
