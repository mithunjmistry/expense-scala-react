package dao

import javax.inject.{Inject, Singleton}
import model.{Expense, ExpenseType, User}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import java.sql.Timestamp

import org.joda.time.DateTime
import org.joda.time.DateTimeZone.UTC

import scala.concurrent.duration._

@Singleton
class ExpenseDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider, userDAOImpl: UserDAOImpl, expenseTypeDAOImpl: ExpenseTypeDAOImpl) extends ExpenseDAO {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val user = userDAOImpl
  val expenseType = expenseTypeDAOImpl

  import dbConfig._
  import user.users
  import expenseType.expenseTypes
  import profile.api._

  class ExpenseTableDef(tag: Tag) extends Table[Expense](tag, "expense") {

    object CustomColumnTypes {
      implicit val jodaDateTimeType =
        MappedColumnType.base[DateTime, Timestamp](
          dt => new Timestamp(dt.getMillis),
          ts => new DateTime(ts.getTime, UTC)
        )
    }
    import CustomColumnTypes._

    def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
    def expense_name = column[String]("expense_name")
    def description = column[Option[String]]("description")
    def amount = column[Double]("amount")
    def created_at = column[Option[DateTime]]("created_at")
    def updated_at = column[Option[DateTime]]("updated_at", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
    def user_id = column[Int]("user_id")
    def expense_type_id = column[Int]("expense_type_id")

    override def * =
      (id, expense_name, description, amount, created_at, updated_at, user_id, expense_type_id) <>(Expense.tupled, Expense.unapply)

    def user = foreignKey("fk_expense_user", user_id, users)(_.id, onDelete=ForeignKeyAction.Cascade, onUpdate=ForeignKeyAction.Cascade)
    def expense_type = foreignKey("fk_expense_expense_type1", expense_type_id, expenseTypes)(_.id, onDelete=ForeignKeyAction.Cascade, onUpdate=ForeignKeyAction.Cascade)
  }

  val expenses = TableQuery[ExpenseTableDef]

  override def add(expenseName: String, description: String, amount: Double, date: DateTime, expense_type: String, user_id: Int): Future[String] = {
    val setup = for {
      eid <- expenseType.findOrCreateExpenseTypeID(expense_type)
      rowAdded <- expenses += Expense(0, expenseName, Some(description), amount, Some(date), null, user_id, eid)
    } yield rowAdded
    db.run(setup).map(res => "Expense added successfully.").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Int): Future[Int] = {
    db.run(expenses.filter(_.id === id).delete)
  }

  override def get(id: Int): Future[Option[Expense]] = {
    db.run(expenses.filter(_.id === id).result.headOption)
  }

  override def listAllExpenses(userID: Int, sorting: (String, String), etype: Option[String], month: Option[String]): Future[Seq[(Expense, ExpenseType, User)]] = {

    implicit val jodaDateTimeType =
    MappedColumnType.base[DateTime, Timestamp](
      dt => new Timestamp(dt.getMillis),
      ts => new DateTime(ts.getTime, UTC)
    )

    val q = for {
      e <- expenses.filter(_.user_id === userID)
                    .filter(ef => month match {
                      case Some(m) => {
                        val monthYear : Array[String] = m.split("-")
                        val startDate = new DateTime(monthYear(1).toInt, monthYear(0).toInt, 1, 0, 0, 0, 0)
                        val endDate = startDate.plusMonths(1)
                        ef.created_at >= startDate && ef.created_at < endDate
                      }
                      case _ => ef.created_at === ef.created_at
                    })
      et <- e.expense_type.filter(etf => etype match {
        case Some(expense_type) => etf.expense_type_name === expense_type.toString
        case _ => etf.expense_type_name === etf.expense_type_name
      })
      u <- e.user
    } yield (e, et, u)

//    sorting match {
//      case ("amount", "asc") => x._1.amount.asc
//      case ("amount", "desc") => x._1.amount.desc
//      case ("created_at", "asc") => x._1.created_at.asc
//      case _ => x._1.created_at.desc
//    }
    db.run(q.sortBy(x => sorting match {
          case ("amount", "asc") => x._1.amount.asc
          case ("amount", "desc") => x._1.amount.desc
          case ("created_at", "asc") => x._1.created_at.asc
          case _ => x._1.created_at.desc
    }
    ).result)
//    db.run(q.sortBy(_._1.created_at.desc).result)
  }

  override def update(id: Int, expenseName: String, description: String, amount: Double, date: DateTime, expense_type: String, user_id: Int): Future[String] = {
    val q = for {
      etid <- expenseType.findOrCreateExpenseTypeID(expense_type)
      e <- expenses.filter(_.id === id).update(Expense(id, expenseName, Some(description), amount, Some(date), null, user_id, etid))
    } yield ()
    db.run(q).map(res => "Expense updated successfully")
      .recover{
        case ex: Exception => ex.getCause.getMessage
      }
  }

  override def getAllDates : Vector[String] = {
    val action = sql""" select distinct(DATE_FORMAT(created_at,'%m-%Y')) from expense order by created_at DESC """.as[String]
    Await.result(db.run(action), 2 seconds)
  }
}
