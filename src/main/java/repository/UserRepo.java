package repository;

import entities.*;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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

    //TODO: US-1 As a user, I would like to see all my rental agreements
    public List<Rental> seeAllTenantsRentals(int tenantId){
        EntityManager em = emf.createEntityManager();
        List<Rental> foundRentals;
        try {
            TypedQuery<Rental> rentalTQ = em.createQuery("SELECT DISTINCT r FROM Rental r join r.tenants rt where rt.id =:tenantsRentalId", Rental.class);
            rentalTQ.setParameter("tenantsRentalId", tenantId);
            foundRentals = rentalTQ.getResultList();
        } finally {
            em.close();
        }

        return foundRentals;
    }

    //TODO: US-2 As a user, I would like to click on a rental agreement and see all details about the house
    public House seeDetailsAboutHouseFromRentalID(int rentalId){
        House foundHouse;
        EntityManager em = emf.createEntityManager();

        try {
            Rental retrievedRental = em.find(Rental.class, rentalId);
                if(retrievedRental == null)
                    throw new EntityNotFoundException("The Rental Entity with ID: "+ rentalId +" was not found or doesn't exist");
            foundHouse = em.find(House.class, retrievedRental.getHouse().getId());
                if(foundHouse == null)
                    throw new EntityNotFoundException("The House Entity with ID: "+ retrievedRental.getHouse().getId() +" was not found or doesn't exist");
        } finally {
            em.close();
        }

        return foundHouse;
    }

}
