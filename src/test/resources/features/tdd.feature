@unit-test
@wip
@tdd
Feature: scenarios for Test-Driven-Development


  Scenario Outline: search full dark elixir drill
    Given screenshot saved as <imagefile>
    When searching full dark elixir drill points
    Then points found are <pointset>

    Examples:
      | imagefile                                      | pointset  |
      | classpath:/features/img/screen_1436831885504.png | [552,283] |