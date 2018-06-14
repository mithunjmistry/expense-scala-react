import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import AppRouter from './routers/AppRouter';
import configureStore from "./store/configureStore";
import { Provider } from 'react-redux';
import Moment from 'moment-timezone';
import axios from "./api/axiosInstance";
import {getFilterAttributesAPI} from "./api/apiURLs";

const store = configureStore();

const jsx = (
  <Provider store={store}>
    <AppRouter />
  </Provider>
);

Moment.tz.setDefault('America/Los_Angeles');
ReactDOM.render(jsx, document.getElementById('root'));
registerServiceWorker();
