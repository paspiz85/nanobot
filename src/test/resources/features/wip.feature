@unit-test
@wip
Feature: single feature to test

  Scenario Outline: parse enemy info
    Given screenshot saved as <imagefile>
    When parsing enemy info
    Then enemy info found is <gold>, <elixir>, <dark_elixir>, <thophy_win>, <thophy_defeat>

    Examples:
      | imagefile                                      | gold   | elixir | dark_elixir | thophy_win | thophy_defeat |
      | classpath:/features/img/base_1435775196561.png | 175494 | null | null         | null         | null            |
#      | classpath:/features/img/base_1435819692500.png | 222 | null | null         | null         | null            |
#      | classpath:/features/img/base_1435819920420.png | 2 | null | null         | null         | null            |
      | classpath:/features/img/base_1435905910030.png | 36438 | null | null         | null         | null            |
      | classpath:/features/img/base_1435905943068.png | 31037 | null | null         | null         | null            |
      | classpath:/features/img/base_1435905992009.png | 28447 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906013388.png | 94741 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906031260.png | 89284 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906058962.png | 48298 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906077198.png | 106328 | null | null         | null         | null            |
      
      | classpath:/features/img/base_1435906098470.png | 95253 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906115317.png | 4275 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906126585.png | 11528 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906144152.png | 38 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906163980.png | 7513 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906182442.png | 35443 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906189462.png | 1998 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906195366.png | 6398 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906203032.png | 29985 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906210498.png | 45245 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906215238.png | 133895 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906220597.png | 18471 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906224691.png | 13319 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906228582.png | 50431 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906232991.png | 2263 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906237740.png | 17451 | null | null         | null         | null            |
      | classpath:/features/img/base_1435906242449.png | 62835 | null | null         | null         | null            |
     