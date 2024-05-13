package com.tripGuide.server.user.repository;

import com.tripGuide.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//@Repository
//@RequiredArgsConstructor
//public class UserRepositoryImpl implements UserRepsitoryCustom{
//
//    private final JPAQueryFactory queryFactory;
//
//    public Optional<User> findByUserIdByProviderAndOauthId(String provider, String oauthId) {
//        return Optional.ofNullable(
//                queryFactory.selectFrom(user)
//                        .where(eqProvider(provider), eqOauthId(oauthId))
//                        .fetchOne()
//        );
//    }
//
//    public BooleanExpression eqProvider(String provider) {
//        return user.provider.eq(provider);
//    }
//
//    public BooleanExpression eqOauthId(String oauthId) {
//        return user.oauthId.eq(oauthId);
//    }
//}
