package ru.artem.NauJava.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.artem.NauJava.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom
{
    private final EntityManager entityManager;
    @Autowired
    public UserRepositoryImpl(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }
    @Override
    public List<User> findByEmail(String email)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        Predicate namePredicate = criteriaBuilder.equal(userRoot.get("email"), email);
        criteriaQuery.select(userRoot).where(namePredicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
    @Override
    public List<User> findByRegistrationDate(LocalDateTime registrationDate)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        Predicate datePredicate = criteriaBuilder.equal(userRoot.get("registration_date"), registrationDate);
        criteriaQuery.select(userRoot).where(datePredicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
