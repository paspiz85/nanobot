@unit-test
Feature: parsing main screen

  Scenario Outline: search attack button
    Given screenshot saved as <imagefile>
    When searching attack button
    Then attack button found at <coords>

    Examples:
      | imagefile                                      | coords |
      | classpath:/features/img/main_1435575631326.png | null   |
      | classpath:/features/img/main_1435601238653.png | [37,9] |
      | classpath:/features/img/main_1435603470723.png | [37,9] |

  Scenario Outline: search full dark elixir drill
    Given screenshot saved as <imagefile>
    When searching full dark elixir drill
    Then full dark elixir drill found at <coords>

    Examples:
      | imagefile                                      | coords    |
      | classpath:/features/img/main_1435601238653.png | [326,325] |
      | classpath:/features/img/main_1435603470723.png | null      |
      