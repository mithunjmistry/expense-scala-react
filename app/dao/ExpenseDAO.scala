package dao

import com.google.inject.ImplementedBy
import model.Expense

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseDAOImpl])
trait ExpenseDAO {
  def add(expense: Expense): Future[String]
  def delete(id: Long): Future[Int]
  def get(id: Long): Future[Option[Expense]]
  def listAllExpenses: Future[Seq[Expense]]
  def update(id: Long, expense: Expense) : Future[String]
}
