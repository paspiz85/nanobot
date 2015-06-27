
Feature: parsing attack screen

   Scenario Outline: parse enemy loot
      Given enemy screenshot saved as <imagefile>
      When parsing loot
      Then loot found is <gold>, <elixir>, <darkelixir>

      Examples:
         |imagefile               |gold    |elixir  |darkelixir |
         |base_1435356695457.png  |85335   |89730   |371        |
         |base_1435356702569.png  |19404   |41256   |368        |

