@unit-test
@deprecated
Feature: parsing main screen


  Scenario Outline: search troops button
    Given screenshot saved as <imagefile>
    When searching troops button point
    Then point found at <coords>

    Examples:
      | imagefile                                       | coords    |
      | classpath:/features/img/main_1436034229185.png  | [38,526]  |


  Scenario Outline: search attack button
    Given screenshot saved as <imagefile>
    When searching attack button point
    Then point found at <coords>

    Examples:
      | imagefile                                      | coords   |
#      | classpath:/features/img/main_1435575631326.png | [63,598] |
#      | classpath:/features/img/main_1435601238653.png | [63,598] |
#      | classpath:/features/img/main_1435603470723.png | [63,598] |


  Scenario Outline: search full gold mine
    Given screenshot saved as <imagefile>
    When searching full gold mine points
    Then points found are <pointset>

    Examples:
      | imagefile                                      | pointset  |
      | classpath:/features/img/screen_1436831885504.png | [280,281]; [289,316]; [336,239]; [393,196]; [440,387]; [496,345] |
      | classpath:/features/img/screen_1436831849088.png | [261,323]; [355,394]; [430,450]; [477,374]; [609,316]            |


  Scenario Outline: search full elixir collector
    Given screenshot saved as <imagefile>
    When searching full elixir collector points
    Then points found are <pointset>

    Examples:
      | imagefile                                      | pointset  |
      | classpath:/features/img/screen_1436831885504.png | [307,250]; [383,377]; [420,165]; [467,172]; [533,320]; [561,243] |
      | classpath:/features/img/screen_1436831849088.png | [298,342]; [402,419]; [430,172]; [458,193]; [505,384]; [561,342] |


  Scenario Outline: search full dark elixir drill
    Given screenshot saved as <imagefile>
    When searching full dark elixir drill points
    Then points found are <pointset>

    Examples:
      | imagefile                                      | pointset  |
      | classpath:/features/img/screen_1436831885504.png | [552,283] |
      | classpath:/features/img/screen_1436831849088.png | [326,325] |
