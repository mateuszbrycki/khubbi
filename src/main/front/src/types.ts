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

    public atUTC(): EventDate {
        return new EventDate(this.value.withZoneSameLocal(ZoneId.UTC))
    }

    public static now(): EventDate {
        return new EventDate(ZonedDateTime.now());
    }

    public static ofDateAndTime(date: string): EventDate {
        return new EventDate(LocalDateTime.parse(date)
            .atZone(ZoneId.SYSTEM));
    }

    public static ofZoneDateAndTime(date: string): EventDate {
        return new EventDate(ZonedDateTime.parse(date));
    }

}

export class Photo {
    readonly date: EventDate;
    readonly id: string;
    readonly description: string;
    readonly photo: string;

    constructor(id: string, date: EventDate, description: string, photo: string) {
        this.date = date;
        this.id = id;
        this.description = description;
        this.photo = photo;
    }
}

export class Note {
    readonly date: EventDate;
    readonly id: string;
    readonly note: string;

    constructor(id: string, date: EventDate, note: string) {
        this.date = date;
        this.id = id;
        this.note = note;
    }
}

export type Event = Photo | Note

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