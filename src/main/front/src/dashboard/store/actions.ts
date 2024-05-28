enum Types {
    LoadShelves = "LOAD_SHELVES",
}

export interface LoadShelves {
    readonly type: Types.LoadShelves
    readonly payload: {
        email: string,
        password: string,
    }
}


const LoadShelvesAction = (email: string, password: string): LoadShelves => ({
    type: Types.LoadShelves,
    payload: {
        email: email,
        password: password,
    }
})


export {
    Types,
    LoadShelvesAction,
}