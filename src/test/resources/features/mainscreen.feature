@unit-test
Feature: parsing main screen


  Scenario Outline: search troops button
    Given screenshot saved as <imagefile>
    When searching troops button point
    Then point found at <coords>

    Examples:
      | imagefile                                       | coords    |
      | classpath:/features/img/main_1436034229185.png  | [38,526] |


  Scenario Outline: search close troops button
    Given screenshot saved as <imagefile>
    When searching close troops button point
    Then point found at <coords>

    Examples:
      | imagefile                                       | coords    |
      | classpath:/features/img/main_1436034229185.png  | null      |
      | classpath:/features/img/train_1435772811358.png | [731,115] |


  Scenario Outline: search attack button
    Given screenshot saved as <imagefile>
    When searching attack button point
    Then point found at <coords>

    Examples:
      | imagefile                                      | coords   |
      | classpath:/features/img/main_1435575631326.png | [63,598] |
      | classpath:/features/img/main_1435601238653.png | [63,598] |
      | classpath:/features/img/main_1435603470723.png | [63,598] |


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


  Scenario Outline: check if rax is full
    Given screenshot saved as <imagefile>
    When checking if camps are full
    Then check is <check>

    Examples:
      | imagefile                                       | check |
      | classpath:/features/img/train_1435769837116.png | false |
      | classpath:/features/img/train_1435772811358.png | true  |


  Scenario Outline: parse troops
    Given screenshot saved as <imagefile>
    When counting troops
    Then troops count is <troops_count>

    Examples:
      | imagefile                                         | troops_count  |
      | classpath:/features/img/screen_1436082310841.png  | [80,70,50,1]  |
      | classpath:/features/img/screen_1436082391016.png  | [78,69,47,1]  |
      | classpath:/features/img/screen_1436082595467.png  | [76,66,45,1]  |
      | classpath:/features/img/screen_1436082626803.png  | [74,62,43,1]  |
      | classpath:/features/img/screen_1436082733324.png  | [65,58,41,1]  |
      | classpath:/features/img/screen_1436082766771.png  | [65,58,38,1]  |
      | classpath:/features/img/screen_1436082785562.png  | [65,58,37,1]  |
      | classpath:/features/img/screen_1436082801864.png  | [65,58,35,1]  |
      | classpath:/features/img/screen_1436082812673.png  | [65,58,31,1]  |
      | classpath:/features/img/screen_1436082826907.png  | [65,58,29,1]  |
      | classpath:/features/img/screen_1436082850104.png  | [65,58,27,1]  |
      | classpath:/features/img/screen_1436082866289.png  | [65,58,24,1]  |
      | classpath:/features/img/screen_1436082879614.png  | [65,58,18,1]  |
      | classpath:/features/img/screen_1436082893899.png  | [65,58,16,1]  |
      | classpath:/features/img/screen_1436082903676.png  | [65,58,13,1]  |
      | classpath:/features/img/screen_1436082920276.png  | [65,58,11,1]  |
      | classpath:/features/img/screen_1436082929588.png  | [65,58,9,1]   |
      | classpath:/features/img/screen_1436082938601.png  | [65,58,8,1]   |
      | classpath:/features/img/screen_1436210992377.png  | [40,99,56,1]  |
      | classpath:/features/img/screen_1436215222084.png  | [53,100,42,1] |
