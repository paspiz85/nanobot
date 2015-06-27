@unit-test
Feature: parsing attack screen

   Scenario Outline: parse enemy loot
      Given enemy screenshot saved as <imagefile>
      When parsing loot
      Then loot found is <gold>, <elixir>, <darkelixir>

      Examples:
         | imagefile                                      | gold   | elixir | darkelixir |
         | classpath:/features/img/base_1435356695457.png | 85335  | 89730  | 371        |
         | classpath:/features/img/base_1435356702569.png | 19404  | 41256  | 368        |
         | classpath:/features/img/base_1435356708541.png | 86703  | 157873 | 641        |
         | classpath:/features/img/base_1435356717499.png | 97093  | 84603  | 81         |
         | classpath:/features/img/base_1435356721666.png | 84125  | 63968  | 385        |
         | classpath:/features/img/base_1435356725776.png | 28923  | 50704  | 342        |
         | classpath:/features/img/base_1435356729468.png | 173202 | 132718 | 963        |
         | classpath:/features/img/base_1435358337068.png | 55059  | 41240  | 531        |
         | classpath:/features/img/base_1435358344374.png | 164806 | 253370 | 862        |
         | classpath:/features/img/base_1435358350820.png | 27046  | 129054 | 449        |
         | classpath:/features/img/base_1435358355546.png | 315272 | 283364 | 640        |

