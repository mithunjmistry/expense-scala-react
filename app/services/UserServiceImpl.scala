package services

import dao.{ExpenseDAO, UserDAO}
import javax.inject.{Inject, Singleton}
import model.User

import scala.concurrent.Future

@Singleton
class UserServiceImpl @Inject()(userDAO: UserDAO) extends UserService {
  def addExpense(user: User): Future[String] = {
    userDAO.add(user)
  }

  def deleteExpense(id: Int): Future[Int] = {
    userDAO.delete(id)
  }

  def getExpense(id: Int): Future[Option[User]] = {
    userDAO.get(id)
  }

  def listAllExpenses: Future[Seq[User]] = {
    userDAO.listAllUsers
  }

  def updateExpense(id: Int, user: User) : Future[String] = {
    userDAO.update(id, user)
  }
}
