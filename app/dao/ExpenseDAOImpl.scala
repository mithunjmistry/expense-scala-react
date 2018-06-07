package dao

import javax.inject.{Inject, Singleton}
import model.Expense
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import java.sql.Timestamp

@Singleton
class ExpenseDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends ExpenseDAO {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ExpenseTableDef(tag: Tag) extends Table[Expense](tag, "expense") {

    def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
    def name = column[String]("name")
    def description = column[String]("description")
    def amount = column[Float]("amount")
    def created_at = column[Timestamp]("created_at", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
    def updated_at = column[Timestamp]("updated_at", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))

    override def * =
      (id, name, description, amount, created_at, updated_at) <>(Expense.tupled, Expense.unapply)
  }

  val expenses = TableQuery[ExpenseTableDef]

  override def add(expense: Expense): Future[String] = {
    db.run(expenses += expense).map(res => "Expense added successfully.").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Long): Future[Int] = {
    db.run(expenses.filter(_.id === id).delete)
  }

  override def get(id: Long): Future[Option[Expense]] = {
    db.run(expenses.filter(_.id === id).result.headOption)
  }

  override def listAllExpenses: Future[Seq[Expense]] = {
    db.run(expenses.result)
  }

  override def update(id: Long, expense: Expense): Future[String] = {
    db.run(expenses.filter(_.id === id).update(expense)).map(res => "Expense updated successfully")
      .recover{
        case ex: Exception => ex.getCause.getMessage
      }
  }
}
