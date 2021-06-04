Feature:  Test getParentDroitAllocation(Map<String, Object> parameters)

  Scenario: One parent with lucrative activity
    Given Create AllocationService
    When I give 1st parent with lucrative activity
    Then The result of One parent with lucrative activity is 1st parent

  Scenario: Two parents with lucrative activity divorced and the child live with the 1st one
    Given Create AllocationService
    When I give 1st parent with lucrative activity
    And I give 2nd parent with lucrative activity
    And the child lives with the 1st parent
    Then The result of Two parents with lucrative activity divorced and the child live with the first one is 1st parent