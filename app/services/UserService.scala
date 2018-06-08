package services

import com.google.inject.ImplementedBy
import model.User

import scala.concurrent.Future

@ImplementedBy(classOf[UserServiceImpl])
trait UserService {
  def addExpense(user: User): Future[String]
  def deleteExpense(id: Int): Future[Int]
  def getExpense(id: Int): Future[Option[User]]
  def listAllExpenses: Future[Seq[User]]
  def updateExpense(id: Int, user: User) : Future[String]
}
