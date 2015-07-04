@unit-test
@wip
@tdd
Feature: single feature to test

  Scenario Outline: parse enemy info
    Given screenshot saved as <imagefile>
    When parsing enemy info
    Then enemy info found is <gold>, <elixir>, <dark_elixir>, <thophy_win>, <thophy_defeat>

    Examples:
      | imagefile                                      | gold   | elixir | dark_elixir | thophy_win | thophy_defeat |
      | classpath:/features/img/base_1435905910030.png | 36438  | 100098 | 121         | null         | null            |
      