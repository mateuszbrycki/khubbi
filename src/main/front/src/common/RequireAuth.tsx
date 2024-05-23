import React from "react";
import {Navigate} from "react-router-dom";


export interface AuthRouteProps {
    readonly children: React.ReactNode;
    readonly isAuthenticated: () => boolean;
}


export const RequireAuth : React.FC<AuthRouteProps> = ({isAuthenticated, children}) => {

    if (!isAuthenticated()) {
        return <Navigate to="/login" />;
    }

    return <>{children}</>;
};




export default RequireAuth