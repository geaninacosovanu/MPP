package repository;


import model.User;

public interface IUserRepository extends IRepository<String, User> {
    boolean exists(User u);
}
