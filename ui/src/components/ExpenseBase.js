import React from 'react';
import { Col, Row, Button, Glyphicon, FormGroup, FormControl, ControlLabel, Form, InputGroup} from 'react-bootstrap';
import { withRouter } from 'react-router-dom';
import {ADD_EXPENSE, EDIT_EXPENSE} from "../api/strings";
import DatePicker from 'react-datepicker';
import Moment from 'moment-timezone';
import 'react-datepicker/dist/react-datepicker-cssmodules.css';
import axios from "../api/axiosInstance";
import {addExpenseAPI, deleteExpenseAPI, getExpenseAPI, updateExpenseAPI} from "../api/apiURLs";
import BaseModal from "../components/BaseModal";

const FieldGroup = ({id, label, componentType, ...props}) => (
  <FormGroup controlId={id}>
    <ControlLabel>{label}</ControlLabel>
    <FormControl componentClass={componentType} {...props} />
  </FormGroup>
);

class ExpenseBase extends React.Component {

  state = {
      title: "Expense",
      date: Moment().tz('America/Los_Angeles'),
      expenseTypes: [],
      otherExpenseType: false,
      expenseTypeGlyph: true,
      error: false,
      saveBtn: false,
      expenseName: "",
      description: "",
      amount: "",
      expenseType: "general",
      expenseID: null,
      showModalState: false
  };

  getExpense = (id, userID) => {
    axios.get(getExpenseAPI(id, userID))
    .then((response) => {
      const data = response.data;
      const date = Moment(data[0].created_at, "MM/DD/YYYY");
      this.setState(() => ({
        expenseName: data[0].expense_name,
        description: data[0].description,
        amount: data[0].amount.toFixed(2),
        expenseType: data[1].expense_type_name,
        expenseID: data[0].id,
        date
      }));
    })
    .catch((error) => {
      console.log(error.response);
    })
  };

  componentDidMount(){
    let expenseTypes = this.props.history.location.state.expenseTypes.filter((value) => value !== "all");
    let action = this.props.match.params.eaction;
    if(action === "add"){
      this.setState(() => ({title: ADD_EXPENSE, expenseTypes}));
    }
    else if(action === "edit"){
      let expenseID = this.props.match.params.expenseID;
      this.setState(() => ({title: EDIT_EXPENSE, expenseTypes}));
      this.getExpense(expenseID, 1);
    }
    else{
      this.props.history.push("/");
    }
  }

  changeRoute = (routeURL) => {
    this.props.history.push(routeURL);
  };

  handleChange = (date) => {
    this.setState(() => ({date}))
  };

  toggleOtherExpenseType = () => {
    this.setState((prevState) => ({otherExpenseType: !prevState.otherExpenseType, expenseTypeGlyph: !prevState.expenseTypeGlyph}));
  };

  handleFormSubmit = (e) => {
      e.preventDefault();
      const expenseName = e.target.expenseName.value;
      const description = e.target.description.value;
      const amount = e.target.amount.value;
      const date = this.state.date;
      let expenseType = e.target.expenseType.value;
      if(this.state.otherExpenseType){
        expenseType = e.target.otherExpenseType.value;
      }

      if(expenseName.length > 0 && amount.length > 0){
        this.setState(() => ({error: false, saveBtn: true}));
        const data = {
          expense_name: expenseName,
          description,
          amount,
          user_id: 1,
          expenseType,
          date
        };
        const headers = {"Content-Type": "application/json"};
        const api = this.state.title === ADD_EXPENSE ? addExpenseAPI : updateExpenseAPI(this.state.expenseID);
        axios.post(api,data, {headers: {...headers}})
          .then((response) => {
            console.log(response.data);
            this.props.history.push("/");
          })
          .catch((error) => {
            this.setState(() => ({saveBtn: false}));
            console.log(error.response);
          });
      }
      else{
        this.setState(() => ({error: true, saveBtn: false}));
      }
  };

  onFormControlChange = (e, inputName) => {
    const value = e.target.value;
    if(inputName === "expenseName"){
      this.setState(() => ({expenseName: value}));
    }
    else if(inputName === "description"){
      this.setState(() => ({description: value}));
    }
    else if(inputName === "amount"){
      this.setState(() => ({amount: value}));
    }
    else if(inputName === "expenseType"){
      this.setState(() => ({expenseType: value}));
    }
  };

  handleModalClose = () => {
    this.setState({ showModalState: false });
  };

  handleModalShow = () => {
    this.setState({ showModalState: true });
  };

  deleteExpense = () => {
    this.setState(() => ({showModalState: false}));
    axios.delete(deleteExpenseAPI(this.state.expenseID)).then((response) => {
      this.props.history.push("/");
    }).catch((error) => {
      console.log(error.response);
    });
  };

  render() {

    const {title, error} = this.state;

    return (
      <div className={"minimum-height container-fluid"}>
        <Row>
          <Col lgOffset={1} mdOffset={1} lg={10} md={10} xs={12} sm={12}>
            <Button bsStyle={"link"} onClick={() => this.changeRoute("/")}>
              <Glyphicon glyph={"chevron-left"}/> Back
            </Button>
            <h4 className={"gray-underline"}>{title}</h4>
            <Form onSubmit={this.handleFormSubmit}>
              <FieldGroup
                id={"expense-name"}
                label={"expense name"}
                componentType={"input"}
                name={"expenseName"}
                value={this.state.expenseName}
                onChange={(e) => this.onFormControlChange(e, "expenseName")}
              />

              <FieldGroup
                id={"description"}
                label={"description"}
                componentType={"textarea"}
                name={"description"}
                value={this.state.description}
                onChange={(e) => this.onFormControlChange(e, "description")}
              />

              <FieldGroup
                id={"amount"}
                label={"amount (in $)"}
                componentType={"input"}
                name={"amount"}
                type="number"
                step="0.01"
                min={0}
                value={this.state.amount}
                onChange={(e) => this.onFormControlChange(e, "amount")}
              />

              <Row>
                <Col lg={4} md={4} sm={12} xs={12}>
                  <FormGroup>
                    <ControlLabel>date</ControlLabel>
                    <DatePicker
                      selected={this.state.date}
                      onChange={this.handleChange}
                      className={"form-control"}
                      name={"date"}
                    />
                  </FormGroup>
                </Col>
                <Col lg={4} md={4} sm={12} xs={12}>
                  <FormGroup>
                    <ControlLabel>expense type</ControlLabel>
                    <InputGroup>
                      <FormControl
                        componentClass={"select"}
                        name={"expenseType"}
                        value={this.state.expenseType}
                        onChange={(e) => this.onFormControlChange(e, "expenseType")}
                        disabled={this.state.otherExpenseType}
                      >
                        {this.state.expenseTypes.map((v) => (
                          <option key={v}>{v}</option>
                        ))}
                      </FormControl>
                      <InputGroup.Button>
                        <Button onClick={this.toggleOtherExpenseType}>
                          <Glyphicon glyph={this.state.expenseTypeGlyph ? "plus" : "remove"}/>
                        </Button>
                      </InputGroup.Button>
                    </InputGroup>
                  </FormGroup>
                </Col>
                {this.state.otherExpenseType &&
                <Col lg={4} md={4} sm={12} xs={12}>
                  <FieldGroup
                    id={"other-expense-type"}
                    label={"other expense type"}
                    componentType={"input"}
                    name={"otherExpenseType"}
                  />
                </Col>}

                {title === EDIT_EXPENSE &&
                <Row className={"margin-bottom-one"}>
                  <Col lg={12} md={12}>
                    <Button bsStyle={"link"} className={"red-color"} onClick={this.handleModalShow}>
                      <Glyphicon glyph={"minus"}/> delete expense
                    </Button>
                  </Col>

                  <BaseModal
                    showModalState={this.state.showModalState}
                    modalTitle={"confirm delete"}
                    handleModalClose={this.handleModalClose}
                    buttonStyle={"danger"}
                    buttonText={"delete"}
                    onConfirmAction={this.deleteExpense}
                  >
                    Are you sure you want to delete the expense?
                  </BaseModal>
                </Row>}

              </Row>

              {error &&
              <Row>
                <Col lg={12} md={12} sm={12} xs={12}>
                  <p className={"red-color"}>Expense Name and Amount are required.</p>
                </Col>
              </Row>}

              <Button bsStyle={"primary"} type={"submit"} disabled={this.state.saveBtn}>
                Save
              </Button>
              <Button className={"margin-left-one"} onClick={() => this.changeRoute("/")} disabled={this.state.saveBtn}>
                Cancel
              </Button>
            </Form>
          </Col>
        </Row>
      </div>
    )
  }
}

export default withRouter(ExpenseBase);
