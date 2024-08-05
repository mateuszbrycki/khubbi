import React from "react";
import {List} from "immutable";
import {AlertMessage} from "../../types";
import {Toast, ToastContainer} from "react-bootstrap";

export interface AlertPanelProps {
    readonly alerts: List<AlertMessage>,
    readonly alertTimeoutMs: number
}

export interface AlertPanelActionProps {
    readonly hideAlert: (alertMessage: AlertMessage) => void
}

export const AlertPanel: React.FC<AlertPanelProps & AlertPanelActionProps> = (props) => {

    const {alerts, alertTimeoutMs, hideAlert} = props

    const alertToasts = alerts.map((alert: AlertMessage) =>
        <Toast key={alert.id}
               id={alert.id.toString()}
               show={true}
               bg={alert.type.bg}
               onClose={() => {
                   hideAlert(alert)
               }} delay={alertTimeoutMs} autohide>
            <Toast.Header>
                <strong className="me-auto">{alert.type.title}</strong>
            </Toast.Header>
            <Toast.Body>
                {alert.message}
            </Toast.Body>
        </Toast>)

    return <>
        {alerts.size <= 0 ? <></> :
            <ToastContainer position="bottom-end" className="p-3" style={{zIndex: 1}}>
                {alertToasts}
            </ToastContainer>
        }
    </>

}