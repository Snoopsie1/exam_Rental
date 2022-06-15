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
public class AdminRepo {

    private static EntityManagerFactory emf;
    private static AdminRepo instance;

    private AdminRepo() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this repository.
     */
    public static AdminRepo getAdminRepo(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdminRepo();
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

    //TODO: US-3 As an admin, I would like to see all tenants currently living in a particular house
    public List<Tenant> getTenantsInSpecificHouse(int houseId){
        EntityManager em = emf.createEntityManager();
        List<Tenant> foundTenants;
        int givenId = 1;

        try {
            House tenantsHouse = em.find(House.class, givenId);
            TypedQuery<Rental> rentalTQ = em.createQuery("SELECT r FROM Rental r WHERE r.house.id is not null and r.house.id = :givenHouseId", Rental.class);
            rentalTQ.setParameter("givenHouseId", tenantsHouse.getId());
            Rental tenantsRental = rentalTQ.getSingleResult();
            TypedQuery<Tenant> tenantTQ = em.createQuery("SELECT DISTINCT t FROM Tenant t join t.rentals tr where tr.id =:tenantsRentalId", Tenant.class);
            tenantTQ.setParameter("tenantsRentalId", tenantsRental.getId());
            foundTenants = tenantTQ.getResultList();
        } finally {
            em.close();
        }

        return foundTenants;
    }

    //TODO: US-4 As an admin I would like to create new rental agreements, tenants and houses
    // House Part
    public House createHouse(String givenAddress, String givenCity, int givenNumOfRooms){
        EntityManager em = emf.createEntityManager();


        House houseToBeCreated = new House(givenAddress, givenCity, givenNumOfRooms);

        try {
            em.getTransaction().begin();
            em.persist(houseToBeCreated);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return houseToBeCreated;
    }

    //Rental Part
    public Rental createRental(String givenStartDate, String givenEndDate, int givenPriceAnnual, int givenDeposit, String givenContactPerson, List<Tenant> givenTenants, House givenHouse){

        EntityManager em = emf.createEntityManager();
        Rental createdRental = new Rental(givenStartDate, givenEndDate, givenPriceAnnual, givenDeposit, givenContactPerson, givenTenants, givenHouse);
        try{
            em.getTransaction().begin();
            em.persist(createdRental);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return createdRental;
    }

    //Tenant Part
    public Tenant createTenant(String givenName, int givenPhoneNum, String givenJob, List<Rental> givenRentals){
        EntityManager em = emf.createEntityManager();
        Tenant createdTenant = new Tenant(givenName, givenPhoneNum, givenJob, givenRentals);

        try {
            em.getTransaction().begin();
            em.persist(createdTenant);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return createdTenant;
    }

    //TODO: US-5 As an admin, I would like to update a rental agreement to change a tenant
    //Process:
    //1. Admin clicks on rental agreement
    //2. Get's clicked Rental Agreement's ID
    //3. Use ID to find agreement
    //4. Make changes via setMethods()
    //5. Update agreements with em.Merge()
    public Rental changeTenantsOnRentalAgreement(int rentalId, List<Tenant> newTenants){
        EntityManager em = emf.createEntityManager();
        Rental foundRental;

        try{
            foundRental = em.find(Rental.class, rentalId);

            foundRental.setTenants(newTenants);
            em.getTransaction().begin();
            em.merge(foundRental);
            List<Rental> newRentals = new ArrayList<>();
            newRentals.add(foundRental);

            for (Tenant newTenant : newTenants) {
                newTenant.setRentals(newRentals);
                em.merge(newTenant);
            }
            em.getTransaction().commit();

        } finally {
            em.close();
        }
        return foundRental;
    }

    //TODO: US-7 As an admin, I would like to delete a rental agreement
    public Rental removeRentalAgreement(int rentalToRemoveId){
        EntityManager em = emf.createEntityManager();
        Rental rentalAgreementToDelete = em.find(Rental.class, rentalToRemoveId);
        try {
            em.getTransaction().begin();
            em.remove(rentalAgreementToDelete);
            em.getTransaction().commit();

            rentalAgreementToDelete = em.find(Rental.class, rentalToRemoveId);
        } finally {
            em.close();
        }
        return rentalAgreementToDelete;
    }

}
