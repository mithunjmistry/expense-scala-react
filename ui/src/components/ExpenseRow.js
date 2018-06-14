import React from 'react';
import { Glyphicon, Button } from 'react-bootstrap';
import {withRouter} from "react-router-dom";
import BaseModal from "../components/BaseModal";


class ExpenseRow extends React.Component{

  state = {
    showModalState: false
  };

  changeRoute = (routeURL) => {
    this.props.history.push({
      pathname: routeURL,
      state: {expenseTypes: this.props.expenseTypes}
    });
  };

  handleModalClose = () => {
    this.setState({ showModalState: false });
  };

  handleModalShow = () => {
    this.setState({ showModalState: true });
  };

  deleteExpense = () => {
    this.setState(() => ({showModalState: false}));
    this.props.deleteExpense(this.props.iden);
  };

  render(){

    return (
      <tr>
        <td>{this.props.iden}</td>
        <td>{this.props.expense_name}</td>
        <td>{this.props.expense_type_name}</td>
        <td>{this.props.description}</td>
        <td>{this.props.created_at}</td>
        <td>{this.props.amount}</td>
        <td>
          <Button bsSize="xsmall" className={"no-border"} onClick={() => this.changeRoute(`/expense/edit/${this.props.iden}`)}>
            <Glyphicon glyph="pencil" />
          </Button>

          <Button bsSize="xsmall" className={"margin-left-five no-border"} onClick={this.handleModalShow}>
            <Glyphicon glyph="remove" />
          </Button>

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
        </td>
      </tr>
    )
  }
}

export default withRouter(ExpenseRow);
