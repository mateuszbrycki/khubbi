import {DateTimeFormatter, LocalDateTime, ZonedDateTime, ZoneId} from "js-joda";
import {v4 as uuidv4} from 'uuid';


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

export class AlertType {

    public static readonly ERROR = new AlertType('ERROR', "Error", "danger");
    public static readonly WARNING = new AlertType('WARNING', "Warning", "warning");
    public static readonly SUCCESS = new AlertType('SUCCESS', "Success", "success");

    private constructor(public readonly type: string, public readonly title: string, public readonly bg: string) {
    }
}

export class AlertMessage {
    public readonly id: string;
    public readonly message: string;
    public readonly type: AlertType;

    constructor(id: string, message: string, type: AlertType) {
        this.id = id
        this.message = message
        this.type = type
    }

    public static error(message: string) {
        return new AlertMessage(uuidv4(), message, AlertType.ERROR)
    }

    public static success(message: string) {
        return new AlertMessage(uuidv4(), message, AlertType.SUCCESS)
    }

    public static warning(message: string) {
        return new AlertMessage(uuidv4(), message, AlertType.WARNING)
    }
}