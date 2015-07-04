@unit-test
@wip
Feature: single feature to test

  Scenario Outline: parse enemy info
    Given screenshot saved as <imagefile>
    When parsing enemy info
    Then enemy info found is <gold>, <elixir>, <dark_elixir>, <thophy_win>, <thophy_defeat>

    Examples:
      | imagefile                                      | gold   | elixir | dark_elixir | thophy_win | thophy_defeat |
      | classpath:/features/img/base_1435775196561.png | 175 | null | null         | null         | null            |
#      | classpath:/features/img/base_1435819692500.png | 222 | null | null         | null         | null            |
#      | classpath:/features/img/base_1435819920420.png | 2 | null | null         | null         | null            |
      | classpath:/features/img/base_1435905910030.png | 3643 | null | null         | null         | null            |
      | classpath:/features/img/base_1435905943068.png | 31037 | null | null         | null         | null            |
      | classpath:/features/img/base_1435905992009.png | 284 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906013388.png | 9474 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906031260.png | 892 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906058962.png | 482 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906077198.png | 10632 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906098470.png | 952 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906115317.png | 427 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906126585.png | 1152 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906144152.png | 38 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906163980.png | 751 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906182442.png | 354 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906189462.png | 1998 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906195366.png | 639 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906203032.png | 29 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906210498.png | 452 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906215238.png | 13385 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906220597.png | 1847 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906224691.png | 1331 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906228582.png | 504 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906232991.png | 226 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906237740.png | 174 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906242449.png | 6283 | null | null         | null         | null            |
     