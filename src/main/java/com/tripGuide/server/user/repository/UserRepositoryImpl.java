package com.tripGuide.server.user.repository;

import com.tripGuide.server.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepsitoryCustom{

//    private final EntityManager em;

    public Optional<User> findByUserIdByProviderAndOauthId(String provider, String oauthId) {
//        String jpql = "SELECT u FROM User u WHERE u.provider = :provider AND u.oauthId = :oauthId";
//        TypedQuery<User> query = em.createQuery(jpql, User.class);
//        query.setParameter("provider", provider);
//        query.setParameter("oauthId", oauthId);
//        return Optional.ofNullable(query.getSingleResult());
        return Optional.of(User.of("asd","asd","asd","asd","asd"));
    }

}
