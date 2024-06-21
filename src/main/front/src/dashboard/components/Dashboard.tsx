import React from "react";

export interface DashboardProps {
}

export interface DashboardActionProps {
    readonly loadUserShelves: (email: string, password: string) => void
}

const Dashboard: React.FC<DashboardProps & DashboardActionProps> = (props) => {

    return <h1>Dashboard</h1>
}

export default Dashboard