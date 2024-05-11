package com.tripGuide.server.user.repository;

import com.tripGuide.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepsitoryCustom {
    Optional<User> findByUserToken(String userToken);
}
