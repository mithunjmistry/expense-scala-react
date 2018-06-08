package dao

import com.google.inject.ImplementedBy
import model.User

import scala.concurrent.Future

@ImplementedBy(classOf[UserDAOImpl])
trait UserDAO {
  def add(user: User): Future[String]
  def delete(id: Int): Future[Int]
  def get(id: Int): Future[Option[User]]
  def listAllUsers: Future[Seq[User]]
  def update(id: Int, user: User) : Future[String]
}
