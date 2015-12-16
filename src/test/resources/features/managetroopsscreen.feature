@unit-test
@deprecated
Feature: parsing attack screen

  Scenario Outline: check if rax is full
    Given screenshot saved as <imagefile>
    When checking if camps are full
    Then check is <check>

    Examples:
      | imagefile                                       | check |
#      | classpath:/features/img/train_1435769837116.png | false |
#      | classpath:/features/img/train_1435772811358.png | true  |


  Scenario Outline: parse troops
    Given screenshot saved as <imagefile>
    When counting troops
    Then troops count is <troops_count>

    Examples:
      | imagefile                                         | troops_count  |
#      | classpath:/features/img/screen_1436082310841.png  | [80:Barb,70:Barb,50:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082391016.png  | [78:Barb,69:Barb,47:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082595467.png  | [76:Barb,66:Barb,45:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082626803.png  | [74:Barb,62:Barb,43:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082733324.png  | [65:Barb,58:Barb,41:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082766771.png  | [65:Barb,58:Barb,38:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082785562.png  | [65:Barb,58:Barb,37:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082801864.png  | [65:Barb,58:Barb,35:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082812673.png  | [65:Barb,58:Barb,31:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082826907.png  | [65:Barb,58:Barb,29:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082850104.png  | [65:Barb,58:Barb,27:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082866289.png  | [65:Barb,58:Barb,24:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082879614.png  | [65:Barb,58:Barb,18:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082893899.png  | [65:Barb,58:Barb,16:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082903676.png  | [65:Barb,58:Barb,13:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082920276.png  | [65:Barb,58:Barb,11:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436082929588.png  | [65:Barb,58:Barb,9:Barb,1:Barbarian King]   |
#      | classpath:/features/img/screen_1436082938601.png  | [65:Barb,58:Barb,8:Barb,1:Barbarian King]   |
#      | classpath:/features/img/screen_1436210992377.png  | [40:Barb,99:Barb,56:Barb,1:Barbarian King]  |
#      | classpath:/features/img/screen_1436215222084.png  | [53:Barb,100:Barb,42:Barb,1:Barbarian King] |
#      | classpath:/features/img/screen_1440826147835.png  | [40:Barb,58:Barb,27:Barb,1:Archer Queen]  |


  Scenario Outline: search close troops button
    Given screenshot saved as <imagefile>
    When searching close troops button point
    Then point found at <coords>

    Examples:
      | imagefile                                       | coords    |
#      | classpath:/features/img/main_1436034229185.png  | null      |
#      | classpath:/features/img/train_1435772811358.png | [731,115] |
