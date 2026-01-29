# Implementation Plan: Account Management

## Overview

Implementation of the account management feature split into two BCE business components: `accounts` for account lifecycle and `transactions` for financial operations. Uses Java 25 with records, sealed interfaces, and jqwik for property-based testing.

## Tasks

- [x] 1. Set up accounts business component entity layer
  - [x] 1.1 Create Currency and Region enums
    - Define Currency enum with USD, EUR, GBP, JPY, CHF, CAD, AUD, CNY, INR, SGD
    - Define Region enum with EUROPE, INDIA, US, ASIA_PACIFIC
    - _Requirements: 7.1, 1.4, 1.5, 1.6_
  
  - [x] 1.2 Create AccountIdentifier sealed interface and implementations
    - Create sealed interface AccountIdentifier with value() and region() methods
    - Create IbanIdentifier record for EUROPE
    - Create IndiaIdentifier record with accountNumber and ifscCode
    - Create UsIdentifier record with accountNumber and routingNumber
    - Create AsiaPacificIdentifier record with accountNumber and bankCode
    - Include toJson() method on each
    - _Requirements: 1.4, 1.5, 1.6_
  
  - [x] 1.3 Create AccountHolder record
    - Define record with id, name, email, address fields
    - Add compact constructor validation for required name
    - Include toJson() and fromJson() methods
    - _Requirements: 1.3_
  
  - [x] 1.4 Create Account record
    - Define record with id, identifier, holder, currency, region, balance, swiftBic, closed, createdAt
    - Add compact constructor validation for non-negative balance
    - Include factory method, withBalance(), asClosed(), toJson(), fromJson()
    - _Requirements: 1.1, 1.7, 2.1_

- [x] 2. Set up accounts business component control layer
  - [x] 2.1 Create IdentifierGenerator interface
    - Implement static methods for generating region-specific identifiers
    - generateIban() for EUROPE with ISO 13616 format
    - generateIndiaIdentifier() with IFSC code
    - generateUsIdentifier() with routing number
    - generateAsiaPacificIdentifier() with bank code
    - _Requirements: 1.4, 1.5, 1.6_
  
  - [ ]* 2.2 Write property test for identifier uniqueness
    - **Property 4: Region-Specific Identifier Generation**
    - **Validates: Requirements 1.4, 1.5, 1.6**
  
  - [x] 2.3 Create AccountProcessor class
    - Implement create() method returning new Account with zero balance
    - Implement find() returning Optional<Account>
    - Implement findByHolder() returning List<Account>
    - Implement close() marking account as closed
    - Implement updateBalance() for balance modifications
    - Use in-memory ConcurrentHashMap for storage
    - _Requirements: 1.1, 2.1, 2.2, 2.3, 8.1, 8.2_
  
  - [ ]* 2.4 Write property tests for AccountProcessor
    - **Property 1: Account Creation Produces Zero-Balance Account**
    - **Property 5: SWIFT_BIC Presence Invariant**
    - **Property 6: Account Retrieval Round-Trip**
    - **Property 8: Holder Account List Completeness**
    - **Validates: Requirements 1.1, 1.7, 2.1, 2.3**

- [x] 3. Set up accounts business component boundary layer
  - [x] 3.1 Create AccountsResource JAX-RS resource
    - POST /accounts - create account
    - GET /accounts/{id} - get account by ID
    - GET /accounts/holder/{holderId} - get accounts by holder
    - DELETE /accounts/{id} - close account
    - Inject AccountProcessor
    - Map JSON requests to domain objects
    - Return appropriate HTTP status codes
    - _Requirements: 1.1, 1.2, 1.3, 2.1, 2.2, 8.1, 8.2_
  
  - [ ]* 3.2 Write property tests for validation
    - **Property 2: Invalid Currency Rejection**
    - **Property 3: Missing Holder Information Rejection**
    - **Property 7: Non-Existent Account Returns Not-Found**
    - **Validates: Requirements 1.2, 1.3, 2.2**

- [x] 4. Checkpoint - Verify accounts component
  - Ensure all tests pass, ask the user if questions arise.

- [x] 5. Set up transactions business component entity layer
  - [x] 5.1 Create TransactionType and TransactionStatus enums
    - Define TransactionType with DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    - Define TransactionStatus with COMPLETED, FAILED, PENDING
    - _Requirements: 3.4, 4.4, 5.4_
  
  - [x] 5.2 Create Transaction record
    - Define record with id, accountId, type, amount, currency, balanceAfter, referenceNumber, relatedAccountId, status, timestamp
    - Include toJson() and fromJson() methods
    - _Requirements: 3.4, 4.4, 5.4, 6.1_

- [x] 6. Set up transactions business component control layer
  - [x] 6.1 Create ExchangeRates interface
    - Implement static convert() method for currency conversion
    - Implement static rate() method returning exchange rate
    - Implement static isSupported() method
    - Use configurable rates (MicroProfile Config or in-memory map)
    - _Requirements: 3.3, 5.3, 7.2_
  
  - [ ]* 6.2 Write property test for currency conversion
    - **Property 21: Currency Conversion Accuracy**
    - **Validates: Requirements 7.2**
  
  - [x] 6.3 Create TransactionProcessor class
    - Inject AccountProcessor for balance operations
    - Inject ExchangeRates for currency conversion
    - Implement deposit() with amount validation and currency conversion
    - Implement withdraw() with balance and amount validation
    - Implement transfer() with atomicity guarantees
    - Implement history() with date filtering and ordering
    - Store transactions in ConcurrentHashMap
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 4.1, 4.2, 4.3, 4.4, 5.1, 5.2, 5.3, 5.4, 5.5, 6.1, 6.2_
  
  - [ ]* 6.4 Write property tests for deposit operations
    - **Property 9: Deposit Increases Balance**
    - **Property 10: Invalid Amount Rejection**
    - **Property 11: Cross-Currency Deposit Conversion**
    - **Property 12: Transaction Recording**
    - **Validates: Requirements 3.1, 3.2, 3.3, 3.4, 4.3**
  
  - [ ]* 6.5 Write property tests for withdrawal operations
    - **Property 13: Withdrawal Decreases Balance**
    - **Property 14: Insufficient Funds Rejection**
    - **Validates: Requirements 4.1, 4.2**
  
  - [ ]* 6.6 Write property tests for transfer operations
    - **Property 15: Transfer Atomicity**
    - **Property 16: Failed Transfer Leaves Accounts Unchanged**
    - **Property 17: Cross-Currency Transfer Conversion**
    - **Property 18: Transfer Creates Matching Reference Transactions**
    - **Validates: Requirements 5.1, 5.2, 5.3, 5.4, 5.5**
  
  - [ ]* 6.7 Write property tests for transaction history
    - **Property 19: Transaction History Ordering**
    - **Property 20: Transaction History Date Filtering**
    - **Validates: Requirements 6.1, 6.2**

- [x] 7. Set up transactions business component boundary layer
  - [x] 7.1 Create TransactionsResource JAX-RS resource
    - POST /transactions/deposit - deposit funds
    - POST /transactions/withdraw - withdraw funds
    - POST /transactions/transfer - transfer between accounts
    - GET /transactions/account/{accountId} - get transaction history with optional date filters
    - Inject TransactionProcessor
    - Map JSON requests to domain objects
    - Return appropriate HTTP status codes
    - _Requirements: 3.1, 3.2, 4.1, 4.2, 5.1, 5.2, 6.1, 6.2, 6.3_

- [x] 8. Checkpoint - Verify transactions component
  - Ensure all tests pass, ask the user if questions arise.

- [x] 9. Implement account closure with transaction validation
  - [x] 9.1 Update AccountProcessor.close() to validate zero balance
    - Check balance is zero before allowing closure
    - Throw exception for non-zero balance
    - _Requirements: 8.1, 8.2_
  
  - [x] 9.2 Update TransactionProcessor to check account closed status
    - Reject operations on closed accounts
    - _Requirements: 8.3_
  
  - [ ]* 9.3 Write property tests for account closure
    - **Property 22: Zero-Balance Account Closure**
    - **Property 23: Non-Zero Balance Closure Rejection**
    - **Property 24: Closed Account Operation Rejection**
    - **Validates: Requirements 8.1, 8.2, 8.3**

- [x] 10. Create exception classes
  - [x] 10.1 Create AccountException base class extending WebApplicationException
    - Create AccountNotFoundException (404)
    - Create InsufficientFundsException (400)
    - Create AccountClosedException (400)
    - Create InvalidAmountException (400)
    - Create UnsupportedCurrencyException (400)
    - Create ExchangeRateUnavailableException (503)
    - _Requirements: 1.2, 2.2, 4.2, 7.3, 8.2, 8.3_

- [x] 11. Final checkpoint - Integration verification
  - Ensure all tests pass, ask the user if questions arise.

- [x] 12. Create system tests in service-st module
  - [x] 12.1 Create AccountsResourceClient interface
    - Define REST client interface for accounts endpoints
    - Use @RegisterRestClient with configKey "service_uri"
    - _Requirements: 1.1, 2.1, 8.1_
  
  - [x] 12.2 Create TransactionsResourceClient interface
    - Define REST client interface for transactions endpoints
    - Use @RegisterRestClient with configKey "service_uri"
    - _Requirements: 3.1, 4.1, 5.1, 6.1_
  
  - [ ]* 12.3 Write system tests for account lifecycle
    - Test create account flow
    - Test retrieve account flow
    - Test close account flow
    - _Requirements: 1.1, 2.1, 8.1_
  
  - [ ]* 12.4 Write system tests for transaction flows
    - Test deposit flow
    - Test withdrawal flow
    - Test transfer flow with same currency
    - Test transfer flow with different currencies
    - _Requirements: 3.1, 4.1, 5.1, 5.3_

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation
- Property tests validate universal correctness properties using jqwik
- In-memory storage (ConcurrentHashMap) used for simplicity - can be replaced with database later
- System tests require the service to be running (`mvn quarkus:dev`)
