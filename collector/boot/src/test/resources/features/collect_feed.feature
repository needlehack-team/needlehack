Feature: Collect Feed
  The event provided will contain all the information needed

  Scenario: Collect new information from an RSS feed
    Given an URI related with an external RSS feed
    When the collecting feed process is fired up
    Then the feeds are collected
    And all the new feed items are stored in a data repository

  Scenario: Collect new information from an RSS feed without content
    Given an URI related with an external RSS feed without content
    When the collecting feed process is fired up
    Then the feeds are collected
    And the items have content and category related with them
    And all the new feed items are stored in a data repository
