@unit-test
@wip
@tdd
Feature: scenarios for Test-Driven-Development

  Scenario Outline: parse troops
    Given screenshot saved as <imagefile>
    When counting troops
    Then troops count is <troops_count>

    Examples:
      | imagefile                                         | troops_count |
      | classpath:/features/img/screen_1436082310841.png  | [63,1160] |
      | classpath:/features/img/screen_1436082391016.png  | [63,1160] |
      | classpath:/features/img/screen_1436082595467.png  | [63,1160] |
      | classpath:/features/img/screen_1436082626803.png  | [63,1160] |
      | classpath:/features/img/screen_1436082733324.png  | [63,1160] |
      | classpath:/features/img/screen_1436082766771.png  | [63,1160] |
      | classpath:/features/img/screen_1436082785562.png  | [63,1160] |
      | classpath:/features/img/screen_1436082801864.png  | [63,1160] |
      | classpath:/features/img/screen_1436082812673.png  | [63,1160] |
      | classpath:/features/img/screen_1436082826907.png  | [63,1160] |
      | classpath:/features/img/screen_1436082850104.png  | [63,1160] |
      | classpath:/features/img/screen_1436082866289.png  | [63,1160] |
      | classpath:/features/img/screen_1436082879614.png  | [63,1160] |
      | classpath:/features/img/screen_1436082893899.png  | [63,1160] |
      | classpath:/features/img/screen_1436082903676.png  | [63,1160] |
      | classpath:/features/img/screen_1436082920276.png  | [63,1160] |
      | classpath:/features/img/screen_1436082929588.png  | [63,1160] |
      | classpath:/features/img/screen_1436082938601.png  | [63,1160] |

