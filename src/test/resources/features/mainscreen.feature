@unit-test
Feature: parsing main screen

  Scenario Outline: search attack button
    Given screenshot saved as <imagefile>
    When searching attack button
    Then attack button found at <coords>

    Examples:
      | imagefile                                      | coords   |
      | classpath:/features/img/main_1435575631326.png | null     |
      | classpath:/features/img/main_1435601238653.png | [62,643] |
      | classpath:/features/img/main_1435603470723.png | [62,643] |

  Scenario Outline: search full gold mine
    Given screenshot saved as <imagefile>
    When searching full gold mine
    Then full gold mine found at <coords>

    Examples:
      | imagefile                                      | coords    |
      | classpath:/features/img/main_1435696574573.png | [449,436] |
      | classpath:/features/img/main_1435698446657.png | [261,225] |

  Scenario Outline: search full elixir collector
    Given screenshot saved as <imagefile>
    When searching full elixir collector
    Then full elixir collector found at <coords>

    Examples:
      | imagefile                                      | coords    |
      | classpath:/features/img/main_1435603470723.png | [458,193] |

  Scenario Outline: search full dark elixir drill
    Given screenshot saved as <imagefile>
    When searching full dark elixir drill
    Then full dark elixir drill found at <coords>

    Examples:
      | imagefile                                      | coords    |
      | classpath:/features/img/main_1435601238653.png | [326,325] |
      | classpath:/features/img/main_1435603470723.png | null      |
