export interface Event {
    readonly note: string;
    readonly date: string;
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