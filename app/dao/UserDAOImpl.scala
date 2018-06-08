package dao

import javax.inject.{Inject, Singleton}
import model.User
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends UserDAO {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTableDef(tag: Tag) extends Table[User](tag, "user") {

    def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
    def full_name = column[String]("full_name")
    def email = column[String]("email", O.Unique)

    override def * =
      (id, full_name, email) <>(User.tupled, User.unapply)
  }

  lazy val users = TableQuery[UserTableDef]

  override def add(user: User): Future[String] = {
    db.run(users += user).map(res => "User added successfully.").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Int): Future[Int] = {
    db.run(users.filter(_.id === id).delete)
  }

  override def get(id: Int): Future[Option[User]] = {
    db.run(users.filter(_.id === id).result.headOption)
  }

  override def listAllUsers: Future[Seq[User]] = {
    db.run(users.result)
  }

  override def update(id: Int, user: User): Future[String] = {
    db.run(users.filter(_.id === id).update(user)).map(res => "User updated successfully")
      .recover{
        case ex: Exception => ex.getCause.getMessage
      }
  }
}
