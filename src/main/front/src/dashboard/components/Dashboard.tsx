import React from "react";
import {Shelf} from "../../types";
import {List} from "immutable";

export interface DashboardProps {
    readonly shelves: List<Shelf>
}

export interface DashboardActionProps {
    readonly loadShelves: () => void
}

const Dashboard: React.FC<DashboardProps & DashboardActionProps> = (props) => {
    const {shelves, loadShelves} = props

    React.useEffect((): void => {
        loadShelves()
    }, [loadShelves])


    return <>
        <h1>Shelves</h1>
        {shelves.map(shelf => <li>{shelf.name}</li>)}
    </>
}

export default Dashboard