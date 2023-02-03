Feature: Test the correct interchange of information between the components.

  Scenario: Create a list, a page inside that list, and then retrieve all the list's pages
    Given login with username "user1" and password "user1"
    And the following lists
      | id | name  |
      | 0  | list1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         |      | list1 |
    Then the count of pages in the list 0 is 1
    And cleanup the environment

  Scenario: Create a tag, a page with that tag, and then retrieve all the tag's pages
    Given login with username "user1" and password "user1"
    And the following tags
      | id | name |
      | 0  | tag1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         | tag1 |       |
    Then the count of pages tagged with 0 is 1
    And cleanup the environment

  Scenario: Create a list, a page, add the page to the list and then retrieve all the list's pages
    Given login with username "user1" and password "user1"
    And the following lists
      | id | name  |
      | 0  | list1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         |      |       |
    When add the page 0 to the list 0
    Then the count of pages in the list 0 is 1
    And cleanup the environment

  Scenario: Create a tag, a page, add the tag to the page and then retrieve all the tags' pages
    Given login with username "user1" and password "user1"
    And the following tags
      | id | name |
      | 0  | tag1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         |      |       |
    When add the tag 0 to the page 0
    Then the count of pages tagged with 0 is 1
    And cleanup the environment

  Scenario: Create a list, a page inside that list, remove the page from the list, and then retrieve all the list's pages
    Given login with username "user1" and password "user1"
    And the following lists
      | id | name  |
      | 0  | list1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         |      | list1 |
    When remove the page 0 from the list 0
    Then the count of pages in the list 0 is 0
    And cleanup the environment

  Scenario: Create a tag, a page tagged with that tag, remove the tag from the page, and then retrieve all the tags' pages
    Given login with username "user1" and password "user1"
    And the following tags
      | id | name |
      | 0  | tag1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         | tag1 |       |
    When remove the tag 0 from the page 0
    Then the count of pages tagged with 0 is 0
    And cleanup the environment

  Scenario: Create a list, a page inside that list, remove the list, and then retrieve all the pages
    Given login with username "user1" and password "user1"
    And the following lists
      | id | name  |
      | 0  | list1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         |      | list1 |
    And the count of all pages is 1
    When remove list 0
    Then the count of all pages is 1
    And cleanup the environment

  Scenario: Create a tag, a page tagged with that tag, remove the tag, and then retrieve all the pages
    Given login with username "user1" and password "user1"
    And the following tags
      | id | name |
      | 0  | tag1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         | tag1 |       |
    And the count of all pages is 1
    When remove tag 0
    Then the count of all pages is 1
    And cleanup the environment

  Scenario: Create a list, a page inside that list, remove the page, and then retrieve all the lists
    Given login with username "user1" and password "user1"
    And the following lists
      | id | name  |
      | 0  | list1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         |      | list1 |
    And the count of all lists is 1
    When remove page 0
    Then the count of all lists is 1
    And cleanup the environment

  Scenario: Create a tag, a page tagged with that tag, remove the tag, and then retrieve all the tags
    Given login with username "user1" and password "user1"
    And the following tags
      | id | name |
      | 0  | tag1 |
    And the following pages
      | id | url                          | img url | tags | lists |
      | 0  | http://localhost:8090/dummy/ |         | tag1 |       |
    And the count of all tags is 1
    When remove page 0
    Then the count of all tags is 1
    And cleanup the environment