import React from 'react';
import {Row, Col, Grid, Panel} from 'react-bootstrap';

const InformationPanel = (props) => (
    <div className={"minimum-height"}>
      <Panel bsStyle="primary">
        <Panel.Heading>
          <Panel.Title componentClass="h3">{props.panelTitle}</Panel.Title>
        </Panel.Heading>
        <Panel.Body>
          <h4>{props.informationHeading}</h4>
          <p>{props.message}</p>
        </Panel.Body>
      </Panel>
    </div>
);

export default InformationPanel;
