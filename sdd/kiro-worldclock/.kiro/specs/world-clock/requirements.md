# Requirements Document

## Introduction

World Clock is a zero-dependency Java 25 CLI application that displays the current time across a curated set of major business hubs. By default, times are presented relative to the user's system default timezone, and the user can override the reference timezone through a command-line option. The application reads no persistent state, writes results to standard output, and reports errors to standard error with a non-zero exit code.

## Glossary

- **World_Clock**: The CLI application that computes and displays current times across business hubs.
- **Business_Hub**: A predefined city with an associated IANA timezone identifier (for example, "New York" mapped to "America/New_York").
- **Hub_Catalog**: The fixed, application-defined collection of Business_Hub entries that World_Clock displays.
- **Reference_Timezone**: The timezone against which the current time is anchored and reported; either the system default or a user-supplied override.
- **System_Default_Timezone**: The timezone reported by the host Java runtime environment (equivalent to `ZoneId.systemDefault()`).
- **Timezone_Override**: A user-supplied IANA timezone identifier that replaces the System_Default_Timezone as the Reference_Timezone.
- **IANA_Timezone_Identifier**: A timezone name from the IANA Time Zone Database (for example, "Europe/Berlin", "Asia/Tokyo").
- **Standard_Output**: The stdout stream where World_Clock writes successful results.
- **Standard_Error**: The stderr stream where World_Clock writes error messages.

## Requirements

### Requirement 1: Display Current Time Across Business Hubs

**User Story:** As a distributed-team professional, I want to see the current time in major business hubs, so that I can coordinate meetings across regions.

#### Acceptance Criteria

1. WHEN World_Clock is invoked, THE World_Clock SHALL write the current time for every Business_Hub in the Hub_Catalog to Standard_Output.
2. THE World_Clock SHALL display, for each Business_Hub, the hub name and the current local time in that Business_Hub's timezone.
3. THE World_Clock SHALL display each Business_Hub time using a 24-hour time format that includes hours and minutes.
4. THE World_Clock SHALL compute each Business_Hub time from a single point-in-time instant so that all displayed times represent the same moment.

### Requirement 2: Use System Default Timezone as Reference

**User Story:** As a user, I want the clock to default to my own timezone, so that I can read the results without extra configuration.

#### Acceptance Criteria

1. WHEN World_Clock is invoked without a Timezone_Override, THE World_Clock SHALL set the Reference_Timezone to the System_Default_Timezone.
2. THE World_Clock SHALL display the current time for the Reference_Timezone alongside the Business_Hub times.
3. THE World_Clock SHALL label the Reference_Timezone entry with its IANA_Timezone_Identifier.

### Requirement 3: Override the Reference Timezone

**User Story:** As a traveler, I want to override the reference timezone, so that I can view business hub times relative to a location other than my system default.

#### Acceptance Criteria

1. WHERE a Timezone_Override is supplied, THE World_Clock SHALL set the Reference_Timezone to the Timezone_Override.
2. WHEN a Timezone_Override that is a valid IANA_Timezone_Identifier is supplied, THE World_Clock SHALL display all Business_Hub times and the Reference_Timezone time for the same instant.
3. IF a supplied Timezone_Override is not a valid IANA_Timezone_Identifier, THEN THE World_Clock SHALL write a descriptive error message to Standard_Error and terminate with a non-zero exit code.

### Requirement 4: Command-Line Interface

**User Story:** As a CLI user, I want a clear command-line interface, so that I can discover and use the override option.

#### Acceptance Criteria

1. WHEN World_Clock is invoked with a help option, THE World_Clock SHALL write usage information describing the timezone override option to Standard_Output and terminate with a zero exit code.
2. IF World_Clock is invoked with an unrecognized option, THEN THE World_Clock SHALL write a descriptive error message to Standard_Error and terminate with a non-zero exit code.
3. WHEN World_Clock completes displaying times successfully, THE World_Clock SHALL terminate with a zero exit code.
