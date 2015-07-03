@unit-test
@wip
Feature: single feature to test

  Scenario Outline: parse enemy info
    Given screenshot saved as <imagefile>
    When parsing enemy info
    Then enemy info found is <gold>, <elixir>, <dark_elixir>, <thophy_win>, <thophy_defeat>

    Examples:
      | imagefile                                      | gold   | elixir | dark_elixir | thophy_win | thophy_defeat |
      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435775196561.png | 175494 | 144734 | 276         | 34         | 15            |
#      | classpath:/features/img/base_1435819692500.png | 2622   | 1268   | null        | 30         | 15            |
#      | classpath:/features/img/base_1435819920420.png | 5239   | 6750   | null        | 32         | 15            |
