package services

import java.sql.Timestamp

import dao.ExpenseDAO
import model.{Expense, ExpenseType, User}
import javax.inject.{Inject, Singleton}
import org.joda.time.DateTime

import scala.concurrent.Future

@Singleton
class ExpenseServiceImpl @Inject()(expenseDAO: ExpenseDAO) extends ExpenseService {
  def addExpense(expenseName: String, description: Option[String] = None, amount: Double, date: DateTime, expense_type: String, user_id: Int = 1): Future[String] = {
    expenseDAO.add(expenseName, description.getOrElse(""), amount, date, expense_type, user_id)
  }

  def deleteExpense(id: Int): Future[Int] = {
    expenseDAO.delete(id)
  }

  def getExpense(id: Int, userID: Int): Future[Option[(Expense, ExpenseType, User)]] = {
    expenseDAO.get(id, userID)
  }

  def listAllExpenses(userID: Int, sorting: (String, String), etype: Option[String] = None, month: Option[String] = None): Future[Seq[(Expense, ExpenseType, User)]] = {
    expenseDAO.listAllExpenses(userID, sorting, etype, month)
  }

  def updateExpense(id: Int, expenseName: String, description: Option[String] = None, amount: Double, date: DateTime, expense_type: String, user_id: Int = 1) : Future[String] = {
    expenseDAO.update(id, expenseName, description.getOrElse(""), amount, date, expense_type, user_id)
  }

  def getAllDates : Vector[String] = {
    expenseDAO.getAllDates
  }
}
