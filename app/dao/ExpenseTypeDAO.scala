package dao

import com.google.inject.ImplementedBy
import model.ExpenseType

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseTypeDAOImpl])
trait ExpenseTypeDAO {
  def add(expenseType: ExpenseType): Future[String]
  def delete(id: Int): Future[Int]
  def get(id: Int): Future[Option[ExpenseType]]
  def listAllExpenseTypes: Future[Seq[ExpenseType]]
  def update(id: Int, expenseType: ExpenseType) : Future[String]
}
