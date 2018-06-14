export const allExpensesAPI = "api/all/expenses";

export const addExpenseAPI = "api/add/expense";

export const allExpenseTypesAPI = "api/all/expensetypes";

export const getAllExpensesAPI = "api/all/expenses";

export const deleteExpenseAPI = (id) => (
  `api/delete/expense/${id}`
);

export const getFilterAttributesAPI = "api/filter/attributes";

export const getExpenseAPI = (id, userID) => (
  `api/get/expense/${id}/${userID}`
);

export const updateExpenseAPI = (id) => (
  `api/update/expense/${id}`
);

export const getStatisticsAPI = "/api/expense/statistics";
