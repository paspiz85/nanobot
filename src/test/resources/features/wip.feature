@unit-test
@wip
Feature: single feature to test

  Scenario Outline: parse enemy info
    Given screenshot saved as <imagefile>
    When parsing enemy info
    Then enemy info found is <gold>, <elixir>, <dark_elixir>, <thophy_win>, <thophy_defeat>

    Examples:
      | imagefile                                      | gold   | elixir | dark_elixir | thophy_win | thophy_defeat |
      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | null         | null            |
      | classpath:/features/img/base_1435905910030.png | 36438  | 100098 | 121         | null         | null            |
      | classpath:/features/img/base_1435905943068.png | 31037  | 2136   | 439         | null         | null            |
      | classpath:/features/img/base_1435905992009.png | 28447  | 16079  | 2320        | null         | null            |
      | classpath:/features/img/base_1435906013388.png | 94741  | 161080 | 712         | null         | null            |
      | classpath:/features/img/base_1435906031260.png | 89284  | 96048  | 205         | null         | null            |
      | classpath:/features/img/base_1435906058962.png | 48298  | 108699 | 963         | null         | null            |
      | classpath:/features/img/base_1435906077198.png | 106328 | 67762  | 3           | null         | null            |
      | classpath:/features/img/base_1435906098470.png | 95253  | 75935  | 50          | null         | null            |
      | classpath:/features/img/base_1435906115317.png | 42075  | 25611  | null        | null         | null            |
      | classpath:/features/img/base_1435906126585.png | 115280 | 69890  | 4           | null         | null            |
      | classpath:/features/img/base_1435906144152.png | 63806  | 51508  | null        | null         | null            |
      | classpath:/features/img/base_1435906163980.png | 75103  | 58602  | 1076        | null         | null            |
      | classpath:/features/img/base_1435906182442.png | 35443  | 306229 | 1762        | null         | null            |
      | classpath:/features/img/base_1435906189462.png | 19980  | 112606 | 717         | null         | null            |
      | classpath:/features/img/base_1435906195366.png | 63986  | 222512 | 313         | null         | null            |
      | classpath:/features/img/base_1435906203032.png | 29985  | 42011  | null        | null         | null            |
      | classpath:/features/img/base_1435906210498.png | 45245  | 7578   | null        | null         | null            |
      | classpath:/features/img/base_1435906215238.png | 133895 | 103513 | 1517        | null         | null            |
      | classpath:/features/img/base_1435906220597.png | 18471  | 2981   | 75          | null         | null            |
      | classpath:/features/img/base_1435906224691.png | 13319  | 9188   | 1           | null         | null            |
      | classpath:/features/img/base_1435906228582.png | 50431  | 81628  | 965         | null         | null            |
      | classpath:/features/img/base_1435906232991.png | 226031 | 2123   | 45          | null         | null            |
      | classpath:/features/img/base_1435906237740.png | 17451  | 10686  | 25          | null         | null            |
      | classpath:/features/img/base_1435906242449.png | 62835  | 31118  | 128         | null         | null            |
     