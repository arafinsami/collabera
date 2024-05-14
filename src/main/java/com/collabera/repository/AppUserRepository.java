package com.collabera.repository;

import com.collabera.entity.AppUser;
import com.collabera.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByUsername(String username);

    AppUser findByPasswordRecoveryCode(String code);
}
