package dao

import javax.inject.{Inject, Singleton}
import model.ExpenseType
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ExpenseTypeDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends ExpenseTypeDAO {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ExpenseTypeTableDef(tag: Tag) extends Table[ExpenseType](tag, "expense_type") {

    def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
    def expense_type_name = column[String]("expense_type_name")

    override def * =
      (id, expense_type_name) <>(ExpenseType.tupled, ExpenseType.unapply)
  }

  lazy val expenseTypes = TableQuery[ExpenseTypeTableDef]

  override def add(expenseType: ExpenseType): Future[String] = {
    db.run(expenseTypes += expenseType).map(res => "ExpenseType added successfully.").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Int): Future[Int] = {
    db.run(expenseTypes.filter(_.id === id).delete)
  }

  override def get(id: Int): Future[Option[ExpenseType]] = {
    db.run(expenseTypes.filter(_.id === id).result.headOption)
  }

  override def listAllExpenseTypes: Future[Seq[ExpenseType]] = {
    db.run(expenseTypes.result)
  }

  override def update(id: Int, expenseType: ExpenseType): Future[String] = {
    db.run(expenseTypes.filter(_.id === id).update(expenseType)).map(res => "ExpenseType updated successfully")
      .recover{
        case ex: Exception => ex.getCause.getMessage
      }
  }
}
