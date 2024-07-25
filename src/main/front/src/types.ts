import {DateTimeFormatter, LocalDateTime, ZonedDateTime, ZoneId} from "js-joda";

export class EventDate {
    value: ZonedDateTime

    public constructor(value: ZonedDateTime) {
        this.value = value
    }

    public toString(): string {
        return this.value.toString();
    }

    public format(format: string): string {
        return this.value.format(DateTimeFormatter.ofPattern(format))
    }

    public toISODateTime(): string {
        return this.value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static now(): EventDate {
        return new EventDate(ZonedDateTime.now());
    }

    public static ofDateAndTime(date: string): EventDate {
        return new EventDate(LocalDateTime.parse(date)
            .atZone(ZoneId.UTC));
    }

}

export interface Event {
    readonly note: string;
    readonly date: EventDate;
    readonly id: string;
}

export interface User {
    readonly id: string;
    readonly email: string;
    readonly createdAt: string;
}

export class EventForms {
    public static readonly NOTE = new EventForms('Note');
    public static readonly PICTURE = new EventForms('Picture');
    public static readonly EVENT = new EventForms('Event');
    public static readonly EXPENSE = new EventForms('Expense');
    public static readonly REMINDER = new EventForms('Reminder');

    private constructor(public readonly name: string) {
    }
}