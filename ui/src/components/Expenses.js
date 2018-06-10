import React from 'react';
import { Glyphicon, Col, Row, Button, FormGroup, FormControl, ControlLabel, Table, Form } from 'react-bootstrap';
import {withRouter} from "react-router-dom";
import Pagination from "react-js-pagination";
import {connect} from "react-redux";
import {changeFilters} from "../actions/filter"
import {ALL, DATE} from "../api/strings";

const SelectGroup = ({options, label, ...props}) => (
  <FormGroup>
    <ControlLabel>{label}</ControlLabel>
    <FormControl componentClass="select" {...props}>
      {options.map((v) => (
        <option value={v} key={v}>{v}</option>
      ))}
    </FormControl>
  </FormGroup>
);

class Expenses extends React.Component{

  state = {
      message: false,
      activePage: 1,
      sortBy: DATE,
      month: ALL,
      type: ALL
  };

  componentDidMount(){
    const {sortBy, month, type} = this.props.filter;
    this.setState(() => ({
      sortBy,
      month,
      type
    }));
  }

  changeRoute = (routeURL) => {
    this.props.history.push(routeURL);
  };

  handlePageChange = (pageNumber) => {
    this.setState(() => ({activePage: pageNumber}));
  };

  handleFilterFormSubmit = (e) => {
    e.preventDefault();
    const sortBy = e.target.sortBy.value;
    const month = e.target.filterDate.value;
    const type = e.target.filterType.value;

    const payload = {
        sortBy,
        month,
        type
    };
    this.props.dispatch(changeFilters(payload));
    this.setState(() => ({
      sortBy,
      month,
      type
    }));
  };

  handleSelectChange = (selectName, e) => {
    const value = e.target.value;
    console.log(value);
    if(selectName === "sortBy"){
      this.setState(() => ({sortBy: value}));
    }
    else if(selectName === "filterMonth"){
      this.setState(() => ({month: value}));
    }
    else if(selectName === "filterType"){
      this.setState(() => ({type: value}));
    }
  };

  render(){

    return (
      <div>
        <Row>
          <Col lg={12} md={12} sm={12} xs={12}>
            <Form onSubmit={this.handleFilterFormSubmit}>
              <Row>
                <Col lg={2} md={2} sm={4}>
                  <SelectGroup options={["date", "amount"]} label={"sort by:"} value={this.state.sortBy} onChange={(e) => this.handleSelectChange("sortBy", e)} name={"sortBy"}/>
                </Col>

                <Col lg={2} md={2} sm={4}>
                  <SelectGroup options={["all", "06-2018", "05-2018"]} label={"month:"} value={this.state.month} onChange={(e) => this.handleSelectChange("filterMonth", e)} name={"filterDate"}/>
                </Col>

                <Col lg={2} md={2} sm={4}>
                  <SelectGroup options={["all", "grocery", "general"]} label={"type:"} value={this.state.type} onChange={(e) => this.handleSelectChange("filterType", e)} name={"filterType"}/>
                </Col>

                <Col lg={2} md={2} sm={12} xs={12}>
                  <ControlLabel className={"apply-change-label"}>apply changes</ControlLabel>
                  <Button type={"submit"}>apply</Button>
                </Col>
              </Row>
            </Form>

            <Row>
              <Col lg={12} md={12} xs={12} sm={12}>
                <Button bsSize="small" bsStyle={"link"} className={"add-expense-btn"} onClick={() => this.changeRoute("/expense/add")}>
                  <Glyphicon glyph="plus" /> add expense
                </Button>
              </Col>
            </Row>
            <Row>
              <Col lg={12} md={12} xs={12} sm={12}>
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
                  <tr>
                    <td>1</td>
                    <td>Rent</td>
                    <td>Type</td>
                    <td>Description</td>
                    <td>06-08-2018</td>
                    <td>$290</td>
                    <td>
                      <Button bsSize="xsmall" className={"no-border"}>
                        <Glyphicon glyph="pencil" />
                      </Button>

                      <Button bsSize="xsmall" className={"margin-left-five no-border"}>
                        <Glyphicon glyph="remove" />
                      </Button>
                    </td>
                  </tr>
                  <tr>
                    <td>1</td>
                    <td>Rent</td>
                    <td>Type</td>
                    <td>Description</td>
                    <td>06-08-2018</td>
                    <td>$290</td>
                    <td>
                      <Button bsSize="xsmall" className={"no-border"}>
                        <Glyphicon glyph="pencil" />
                      </Button>

                      <Button bsSize="xsmall" className={"margin-left-five no-border"}>
                        <Glyphicon glyph="remove" />
                      </Button>
                    </td>
                  </tr>
                  <tr>
                    <td>1</td>
                    <td>Rent</td>
                    <td>Type</td>
                    <td>Description</td>
                    <td>06-08-2018</td>
                    <td>$290</td>
                    <td>
                      <Button bsSize="xsmall" className={"no-border"}>
                        <Glyphicon glyph="pencil" />
                      </Button>

                      <Button bsSize="xsmall" className={"margin-left-five no-border"}>
                        <Glyphicon glyph="remove" />
                      </Button>
                    </td>
                  </tr>
                  </tbody>
                </Table>
              </Col>
            </Row>

            <Row>
              <Col lg={12} md={12} xs={12} sm={12}>

                <div>
                  <Pagination
                    activePage={this.state.activePage}
                    itemsCountPerPage={10}
                    totalItemsCount={450}
                    pageRangeDisplayed={5}
                    hideFirstLastPages={true}
                    onChange={this.handlePageChange}
                  />
                </div>
              </Col>
            </Row>
          </Col>
        </Row>


      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    filter: state.filter
  };
};

export default connect(mapStateToProps)(withRouter(Expenses));
