package dao

import com.google.inject.ImplementedBy
import model.ExpenseType
import slick.dbio.DBIO

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseTypeDAOImpl])
trait ExpenseTypeDAO {
  def add(expenseType: ExpenseType): Future[String]
  def delete(id: Int): Future[Int]
  def get(id: Int): Future[Option[ExpenseType]]
  def findExpenseTypeId(expense_type_name: String): DBIO[Option[Int]]
  def findOrCreateExpenseTypeID(expense_type_name: String): DBIO[Int]
  def listAllExpenseTypes: Future[Seq[ExpenseType]]
  def update(id: Int, expenseType: ExpenseType) : Future[String]
}
