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

  override def listAllExpenseTypes: Future[Seq[String]] = {
    db.run(expenseTypes.map(_.expense_type_name).result)
  }

  override def findExpenseTypeId(expense_type_name: String): DBIO[Option[Int]] =
    expenseTypes.filter(_.expense_type_name === expense_type_name).map(_.id).result.headOption

  lazy val insertNewExpenseType = expenseTypes returning expenseTypes.map(_.id)

  override def findOrCreateExpenseTypeID(expense_type_name: String): DBIO[Int] =
    findExpenseTypeId(expense_type_name).flatMap { expenseTypeID =>
      expenseTypeID match {
        case Some(id) => DBIO.successful(id)
        case None     => insertNewExpenseType += ExpenseType(0, expense_type_name)
      }
    }

  override def update(id: Int, expenseType: ExpenseType): Future[String] = {
    db.run(expenseTypes.filter(_.id === id).update(expenseType)).map(res => "ExpenseType updated successfully")
      .recover{
        case ex: Exception => ex.getCause.getMessage
      }
  }
}
