import React from 'react';
import { Glyphicon, Col, Row, Button, FormGroup, FormControl, ControlLabel, Table, Form, Overlay, Popover } from 'react-bootstrap';
import {withRouter} from "react-router-dom";
import Pagination from "react-js-pagination";
import {connect} from "react-redux";
import {changeFilters} from "../actions/filter"
import {ALL, DATE} from "../api/strings";
import {deleteExpenseAPI, getAllExpensesAPI} from "../api/apiURLs";
import axios from "../api/axiosInstance";
import ExpenseRow from "../components/ExpenseRow";

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
      type: ALL,
      expenses: [],
      totalItemsCount: 0
  };

  getExpensesList = (sortBy, month, type) => {

    let params = {
      sortColumn: sortBy === "date" ? "created_at" : "amount"
    };

    if(type !== "all"){
      params = {...params, etype: type}
    }
    if(month !== "all"){
      params = {...params, month}
    }

    axios.get(getAllExpensesAPI, {
      params
    }).then((response) => {
      this.setState(() => ({
        expenses: response.data,
        totalItemsCount: response.data.length
      }));
    })
      .catch((error) => {
        console.log(error.response);
      });
  };

  componentDidMount(){
    const {sortBy, month, type} = this.props.filter;
    this.setState(() => ({
      sortBy,
      month,
      type
    }));
    this.getExpensesList(sortBy, month, type);
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
    this.getExpensesList(sortBy, month, type);
  };

  handleSelectChange = (selectName, e) => {
    const value = e.target.value;
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

  deleteExpense = (id) => {
    this.setState((prevState) => ({
      expenses: prevState.expenses.filter((value) => (
        value[0].id != id
      ))
    }));

    axios.delete(deleteExpenseAPI(id)).then((response) => {
      console.log(response.data);
    }).catch((error) => {
      console.log(error.response);
    });
  };

  render(){

    let items = [];
    const {activePage, expenses} = this.state;
    const end = (activePage*10) - 1;
    const start = end - 9;
    for (let i = start; i <= end; i++ ) {
      if(i < expenses.length) {
        items.push(
          <ExpenseRow
            key={i}
            iden={expenses[i][0].id}
            expense_name={expenses[i][0].expense_name}
            expense_type_name={expenses[i][1].expense_type_name}
            description={expenses[i][0].description}
            created_at={expenses[i][0].created_at}
            amount={expenses[i][0].amount}
            deleteExpense={this.deleteExpense}
          />
          );
      }else{
        break;
      }
    }

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
                  <SelectGroup options={this.props.dateFilter} label={"month:"} value={this.state.month} onChange={(e) => this.handleSelectChange("filterMonth", e)} name={"filterDate"}/>
                </Col>

                <Col lg={2} md={2} sm={4}>
                  <SelectGroup options={this.props.expenseTypeFilter} label={"type:"} value={this.state.type} onChange={(e) => this.handleSelectChange("filterType", e)} name={"filterType"}/>
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
                    {items}
                  </tbody>
                </Table>
              </Col>
            </Row>

            {this.state.expenses.length > 0 &&
            <Row>
              <Col lg={12} md={12} xs={12} sm={12}>

                <div>
                  <Pagination
                    activePage={this.state.activePage}
                    itemsCountPerPage={10}
                    totalItemsCount={this.state.totalItemsCount}
                    pageRangeDisplayed={3}
                    hideFirstLastPages={true}
                    onChange={this.handlePageChange}
                  />
                </div>
              </Col>
            </Row>}
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
