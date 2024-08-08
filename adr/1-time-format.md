# [ADR-1] Use Offset Date Time for Data Storage and Exchange

## TL;DR;

`Offset Date Time` - backend, database, communication

`Zoned Date Time` - frontend components displaying data to a user

## Context

Users actions are persisted along with the time the action happened.
These times are put in the context of their timezones.
If the application shows any date and time of an action, the time should be adjusted to the user's timezone.

## Options

### Store and exchange dates with the original timezone

This option assumes that there is no data conversion. Any date and time is stored with the original user's timezone.

### Store and exchange dates with the timezone offset

When date is recorded (e.g. after user's action), it's converted into a date with timezone offset. The only places where
the date
is converted back into zoned version are frontend components.

## Decision

For storage and data transition use timezone offset (`DateTimeFormatter.ISO_OFFSET_DATE_TIME`).
The only places where a date should be converted into user's timezone are frontend components where the date is
displayed to the user.

## Consequences

1. It becomes easier to manipulate date.
1. Testing setup is simplified so that there are no differences between local machines and remote pipelines (especially
   problematic in snapshot tests).
1. Sorting any types of components gets easier as there is no burden required to adjusting dates to the same baseline.
1. Since there is no timezone, the information about daylight savings changes is lost. A user from Poland will have
   wrong time when displaying in the summer an event that has been added in the winter.

# Links

1. [Differences Between ZonedDateTime and OffsetDateTime](https://www.baeldung.com/java-zoneddatetime-offsetdatetime)
