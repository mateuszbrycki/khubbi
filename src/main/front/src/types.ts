export interface Event {
    readonly name: string;
    readonly id: string;
}

export interface User {
    readonly id: string;
    readonly email: string;
    readonly createdAt: string;
}