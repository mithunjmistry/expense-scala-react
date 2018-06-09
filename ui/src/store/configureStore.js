import { createStore, combineReducers } from 'redux';
import filterReducer from "../reducers/filter";

const rootReducer = combineReducers({
  filter: filterReducer
});

export default () => {
  const store = createStore(
    rootReducer
  );

  return store;
};
