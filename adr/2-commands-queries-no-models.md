# [ADR-2] Input Port Commands and Queries Should Accept Only Value Objects From the Model

## TL;DR;

Prefer

```java
record AddNoteCommand(UserId owner, EventDate date, String note) {
}
```

over

```java
record AddNoteCommand(User owner, ZonedDateTime date, String note) {
}
```

## Context

Any external entities communicate with the application domain using ports. To perform any
action ports need to take action's parameters.

In the application, input ports (`com.bookkeeper.app.application.port.in`) accept either commands
(action modifying state) or queries (no modification).

## Options

Let's consider the following port:

```java
public interface AddNoteUseCase {
    Try<Note> addNote(AddNoteCommand command);
}
```

### Commands and queries accept primitive types and model objects

```java
record AddNoteCommand(User owner, ZonedDateTime date, String note) {
}
```

To call a port, you need to retrieve/build the whole `User` object. You need to probably call
another port so that the user's data is retrieved from the database.

Moreover, you expose date implementation details (`ZoneDateTime`) to the port. Once you decided to
change date type, you need to refactor the port and subsequent adapters.

### Commands and queries accept value objects

```java
record AddNoteCommand(UserId owner, EventDate date, String note) {
}
```

To call a port, you need to build simple value objects. You don't need to know which port you need to call
to query user data.

The port is the actual gate to the domain logic. You don't need any sophisticated logic to call the domain service.

The port is not aware of how the actual event date is represented. Once changed, the port is not affected at all.
It's highly probable that the adapter won't be affected either.

## Decision

Input Ports commands and queries should accept only value objects from the model and primitive types.

## Consequences

1. Input Ports are easier to be called because user doesn't need to build/retrieve actual model objects.
1. Input Ports clients (adapters) are resilient to any change in the model itself. Adapters and domain
   are separated with port interface (commands and queries).
1. Adapter doesn't need to know what the model looks like.
1. Any changes in value objects MAY affect adapters.
1. The way the objects would be built in adapters is not reflected in the actual model. The argument of "an object
   must be easy to be built by adapter" is not important at all.
1. No unnecessary data is exposed while building the command/query.
1. Adapters logic is simplified. An adapter has adapter-specific code, the domain logic is implemented behind the port.
