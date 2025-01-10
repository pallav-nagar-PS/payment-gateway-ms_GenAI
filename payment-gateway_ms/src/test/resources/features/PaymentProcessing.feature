Feature: Payment Processing
  Scenario: Successfully process a payment
    Given a valid payment request
    When the payment is processed
    Then the response should indicate a successful transaction
