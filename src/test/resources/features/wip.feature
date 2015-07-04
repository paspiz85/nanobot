@unit-test
@wip
Feature: single feature to test

  Scenario Outline: parse enemy info
    Given screenshot saved as <imagefile>
    When parsing enemy info
    Then enemy info found is <gold>, <elixir>, <dark_elixir>, <thophy_win>, <thophy_defeat>

    Examples:
      | imagefile                                      | gold   | elixir | dark_elixir | thophy_win | thophy_defeat |
      | classpath:/features/img/base_1435775196561.png | 175494 | 1444 | null         | null         | null            |
#      | classpath:/features/img/base_1435819692500.png | 222 | null | null         | null         | null            |
#      | classpath:/features/img/base_1435819920420.png | 2 | null | null         | null         | null            |
      | classpath:/features/img/base_1435905910030.png | 36438  | 1000 | null         | null         | null            |
      | classpath:/features/img/base_1435905943068.png | 31037  | 216 | null         | null         | null            |
      | classpath:/features/img/base_1435905992009.png | 28447  | 160 | null         | null         | null            |
      | classpath:/features/img/base_1435906013388.png | 94741  | 1610 | null         | null         | null            |
      | classpath:/features/img/base_1435906031260.png | 89284  | 604 | null         | null         | null            |
      | classpath:/features/img/base_1435906058962.png | 48298  | 106 | null         | null         | null            |
      | classpath:/features/img/base_1435906077198.png | 106328 | 662 | null         | null         | null            |
      | classpath:/features/img/base_1435906098470.png | 95253  | null | null         | null         | null            |
      | classpath:/features/img/base_1435906115317.png | 42075  | 2611 | null         | null         | null            |
      | classpath:/features/img/base_1435906126585.png | 115280 | 60 | null         | null         | null            |
      | classpath:/features/img/base_1435906144152.png | 63806  | 10 | null         | null         | null            |
      | classpath:/features/img/base_1435906163980.png | 75103  | 602 | null         | null         | null            |
      | classpath:/features/img/base_1435906182442.png | 35443  | 0622 | null         | null         | null            |
      | classpath:/features/img/base_1435906189462.png | 19980  | 112606 | null         | null         | null            |
      | classpath:/features/img/base_1435906195366.png | 63986  | 22212 | null         | null         | null            |
      | classpath:/features/img/base_1435906203032.png | 29985  | 42011 | null         | null         | null            |
      | classpath:/features/img/base_1435906210498.png | 45245  | null | null         | null         | null            |
      | classpath:/features/img/base_1435906215238.png | 133895 | 101 | null         | null         | null            |
      | classpath:/features/img/base_1435906220597.png | 18471  | 21 | null         | null         | null            |
      | classpath:/features/img/base_1435906224691.png | 13319  | 1 | null         | null         | null            |
      | classpath:/features/img/base_1435906228582.png | 50431  | 162 | null         | null         | null            |
      | classpath:/features/img/base_1435906232991.png | 226031 | 212 | null         | null         | null            |
      | classpath:/features/img/base_1435906237740.png | 17451  | 1066 | null         | null         | null            |
      | classpath:/features/img/base_1435906242449.png | 62835  | 111 | null         | null         | null            |
     