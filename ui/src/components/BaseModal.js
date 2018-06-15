import React from 'react';
import {Modal, Button} from "react-bootstrap";

const BaseModal = (props) => (
<Modal show={props.showModalState} onHide={props.handleModalClose}>
    <Modal.Header closeButton>
      <Modal.Title>{props.modalTitle}</Modal.Title>
    </Modal.Header>

    <Modal.Body>
      {props.children}
    </Modal.Body>

    <Modal.Footer>
      <Button onClick={props.handleModalClose}>cancel</Button>
      {props.buttonStyle &&
      <Button bsStyle={props.buttonStyle} onClick={props.onConfirmAction}>{props.buttonText}</Button>}
    </Modal.Footer>
</Modal>);

export default BaseModal;
