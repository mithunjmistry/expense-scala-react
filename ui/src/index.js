import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import AppRouter from './routers/AppRouter';

ReactDOM.render(<AppRouter />, document.getElementById('root'));
registerServiceWorker();
