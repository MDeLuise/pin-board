Feature: Test the correct functionality of the authentication.

  Scenario: call GET /page without authentication. Request should be denied.
    When call GET "/page"
    Then receive status code of 403

  Scenario: call GET /page/0 without authentication. Request should be denied.
    When call GET "/page/0"
    Then receive status code of 403

  Scenario: call POST /page without authentication. Request should be denied.
    When call POST "/page" with body "{}"
    Then receive status code of 403

  Scenario: call PUT /page/0 without authentication. Request should be denied.
    When call PUT "/page/0" with body "{}"
    Then receive status code of 403

  Scenario: call DELETE /page/0 without authentication. Request should be denied.
    When call DELETE "/page/0" with body "{}"
    Then receive status code of 403


  Scenario: call GET /list without authentication. Request should be denied.
    When call GET "/list"
    Then receive status code of 403

  Scenario: call GET /list/0 without authentication. Request should be denied.
    When call GET "/list/0"
    Then receive status code of 403

  Scenario: call POST /list without authentication. Request should be denied.
    When call POST "/list" with body "{}"
    Then receive status code of 403

  Scenario: call PUT /list/0 without authentication. Request should be denied.
    When call PUT "/list/0" with body "{}"
    Then receive status code of 403

  Scenario: call DELETE /list/0 without authentication. Request should be denied.
    When call DELETE "/list/0" with body "{}"
    Then receive status code of 403

  Scenario: call DELETE /list/0/pages without authentication. Request should be denied.
    When call DELETE "/list/0/pages" with body "{}"
    Then receive status code of 403


  Scenario: call GET /tag without authentication. Request should be denied.
    When call GET "/tag"
    Then receive status code of 403

  Scenario: call GET /tag/0 without authentication. Request should be denied.
    When call GET "/tag/0"
    Then receive status code of 403

  Scenario: call POST /tag without authentication. Request should be denied.
    When call POST "/tag" with body "{}"
    Then receive status code of 403

  Scenario: call PUT /tag/0 without authentication. Request should be denied.
    When call PUT "/tag/0" with body "{}"
    Then receive status code of 403

  Scenario: call DELETE /tag/0 without authentication. Request should be denied.
    When call DELETE "/tag/0" with body "{}"
    Then receive status code of 403

  Scenario: call DELETE /tag/0/pages without authentication. Request should be denied.
    When call DELETE "/tag/0/pages" with body "{}"
    Then receive status code of 403


  Scenario: Login correctly.
    Given login with username "user1" and password "user1"
    Then receive status code of 200

  Scenario: Login denied with username correct and wrong password.
    Given login with username "user1" and password "wrongPassword"
    Then receive status code of 500

  Scenario: Login denied with username and password wrong.
    Given login with username "userDoesNotExist" and password "user"
    Then receive status code of 500