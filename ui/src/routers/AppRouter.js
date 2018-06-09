import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import HomePage from "../components/HomePage";
import Header from "../components/Header";
import ExpenseBase from "../components/ExpenseBase";

const appRouter = () => (
    <BrowserRouter>
        <div>
            <Header/>
            <Switch>
                <Route path="/" component={HomePage} exact={true} />
                <Route path="/expense/:eaction/:expenseID?" component={ExpenseBase} exact={true} />
            </Switch>
        </div>
    </BrowserRouter>
);

export default appRouter;
