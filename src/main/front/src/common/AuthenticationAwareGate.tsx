import React from "react";
import {Navigate, useLocation} from "react-router-dom";

// TODO mateusz.brycki consider resigning from <Navigate>, dispatch actions instead, maybe current page will be available
export interface AuthRouteProps {
    readonly children: React.ReactNode;
    readonly isAuthenticated: boolean;
    readonly redirectToIfLogged?: string;
    readonly redirectToIfNotLogged?: string;
}


export const AuthenticationAwareGate: React.FC<AuthRouteProps> = ({
                                                                      isAuthenticated,
                                                                      children,
                                                                      redirectToIfLogged,
                                                                      redirectToIfNotLogged
                                                                  }) => {

    const currentURL = useLocation().pathname

    if (!isAuthenticated) {
        if (redirectToIfNotLogged) {
            return <Navigate to={redirectToIfNotLogged}/>;
        }

        if (currentURL != "/login") {
            return <Navigate to="/login"/>;
        }
    }

    if (isAuthenticated && redirectToIfLogged) {
        return <Navigate to={redirectToIfLogged}/>;
    }

    return <>{children}</>;
};


export default AuthenticationAwareGate