@unit-test
@wip
Feature: parsing attack screen

  Scenario Outline: parse enemy info
    Given screenshot saved as <imagefile>
    When parsing enemy info
    Then enemy info found is <gold>, <elixir>, <dark_elixir>, <thophy_win>, <thophy_defeat>

    Examples:
      | imagefile                                      | gold   | elixir | dark_elixir | thophy_win | thophy_defeat |
      | classpath:/features/img/base_1435356695457.png | 85335  | 89730  | 371         | 15         | 15            |
      | classpath:/features/img/base_1435356702569.png | 19404  | 41256  | 368         | 26         | 15            |
      | classpath:/features/img/base_1435356708541.png | 86703  | 157873 | 641         | 32         | 15            |
      | classpath:/features/img/base_1435356717499.png | 97093  | 84603  | 81          | 23         | 15            |
      | classpath:/features/img/base_1435356721666.png | 84125  | 63968  | 385         | 23         | 15            |
      | classpath:/features/img/base_1435356725776.png | 28923  | 50704  | 342         | 28         | 15            |
      | classpath:/features/img/base_1435356729468.png | 173202 | 132718 | 963         | 20         | 15            |
      | classpath:/features/img/base_1435358337068.png | 55059  | 41240  | 531         | 34         | 15            |
      | classpath:/features/img/base_1435358344374.png | 164806 | 253370 | 862         | 32         | 15            |
      | classpath:/features/img/base_1435358350820.png | 27046  | 129054 | 449         | 28         | 15            |
      | classpath:/features/img/base_1435358355546.png | 315272 | 283364 | 640         | 22         | 15            |

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
