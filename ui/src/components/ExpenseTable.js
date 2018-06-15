import React from 'react';
import {Row, Col, Table} from "react-bootstrap";
import LoadingHOC from "../components/LoadingHOC";

const ExpenseTable = (props) => (
  <Row>
    <Col lg={12} md={12} xs={12} sm={12}>
      <div className={"padding-bottom-ten"}>
        <span className={"light-silver"}>Load time: {props.loadingTime} seconds</span>
      </div>
      <Table responsive bordered hover>
        <thead>
        <tr>
          <th>#</th>
          <th>expense name</th>
          <th>expense type</th>
          <th>description</th>
          <th>date</th>
          <th>amount</th>
          <th>action</th>
        </tr>
        </thead>
        <tbody>
          {props.expenses}
        </tbody>
      </Table>
    </Col>
  </Row>
);

export default LoadingHOC("expenses")(ExpenseTable);
