import React from "react";
import {Navigate} from "react-router-dom";


export interface AuthRouteProps {
    readonly children: React.ReactNode;
    readonly isAuthenticated: () => boolean;
    readonly redirectToIfLogged?: string;
    readonly redirectToIfNotLogged?: string;
}


export const AuthenticationAwareGate: React.FC<AuthRouteProps> = ({
                                                                      isAuthenticated,
                                                                      children,
                                                                      redirectToIfLogged,
                                                                      redirectToIfNotLogged
                                                                  }) => {

    if (!isAuthenticated()) {
        if (redirectToIfNotLogged) {
            return <Navigate to={redirectToIfNotLogged}/>;
        }
        // TODO mateusz.brycki this causes problem for the login page - the infinite redirection loop
        // return <Navigate to="/login"/>;
    }

    if (isAuthenticated() && redirectToIfLogged) {
        return <Navigate to={redirectToIfLogged}/>;
    }

    return <>{children}</>;
};


export default AuthenticationAwareGate