package dao

import javax.inject.{Inject, Singleton}
import model.{Expense, ExpenseType, User}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.sql.Timestamp

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

    def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
    def expense_name = column[String]("expense_name")
    def description = column[Option[String]]("description")
    def amount = column[Double]("amount")
    def created_at = column[Option[Timestamp]]("created_at")
    def updated_at = column[Option[Timestamp]]("updated_at", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
    def user_id = column[Int]("user_id")
    def expense_type_id = column[Int]("expense_type_id")

    override def * =
      (id, expense_name, description, amount, created_at, updated_at, user_id, expense_type_id) <>(Expense.tupled, Expense.unapply)

    def user = foreignKey("fk_expense_user", user_id, users)(_.id, onDelete=ForeignKeyAction.Cascade, onUpdate=ForeignKeyAction.Cascade)
    def expense_type = foreignKey("fk_expense_expense_type1", expense_type_id, expenseTypes)(_.id, onDelete=ForeignKeyAction.Cascade, onUpdate=ForeignKeyAction.Cascade)
  }

  val expenses = TableQuery[ExpenseTableDef]

  override def add(expenseName: String, description: String, amount: Double, date: Timestamp, expense_type: String, user_id: Int): Future[String] = {
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

  override def listAllExpenses(userID: Int): Future[Seq[(Expense, ExpenseType, User)]] = {
//    val q : Query[(ExpenseTableDef, expenseType.ExpenseTypeTableDef), (Expense, ExpenseType), Seq] =
//    val q = for {
//      (e, et) <- expenses join  expenseType.expenseTypes on (_.expense_type_id === _.id)
//      (_, u) <- expenses join user.users on (_.user_id === _.id)
//    } yield (e, et, u)
      val q = for {
        e <- expenses.filter(_.user_id === userID)
        et <- e.expense_type
        u <- e.user
      } yield (e, et, u)
    db.run(q.sortBy(_._1.created_at.desc).result)
//    db.run(q.join(user.users).on(_._1.user_id === _.id).result)
//      db.run(expenses.join(expenseType.expenseTypes).on(_.expense_type_id === _.id).result)
  }

  override def update(id: Int, expense: Expense): Future[String] = {
    db.run(expenses.filter(_.id === id).update(expense)).map(res => "Expense updated successfully")
      .recover{
        case ex: Exception => ex.getCause.getMessage
      }
  }
}
