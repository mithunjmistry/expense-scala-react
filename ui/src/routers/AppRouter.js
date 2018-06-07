import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import HomePage from "../components/HomePage";

const appRouter = () => (
    <BrowserRouter>
        <div>
            <Switch>
                <Route path="/" component={HomePage} exact={true} />
            </Switch>
        </div>
    </BrowserRouter>
);

export default appRouter;
