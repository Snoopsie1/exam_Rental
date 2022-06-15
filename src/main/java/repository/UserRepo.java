package repository;

import entities.Role;
import entities.User;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author lam@cphbusiness.dk
 */
public class UserRepo {

    private static EntityManagerFactory emf;
    private static UserRepo instance;

    private UserRepo() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this repository.
     */
    public static UserRepo getUserRepo(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserRepo();
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User registerUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        User user = new User(username, password);
        user.addRole(Role.RoleNames.USER);

        try {
            User validate = em.find(User.class, username);
            if (validate != null) {
                throw new AuthenticationException("User already exists");
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return user;
    }

}
