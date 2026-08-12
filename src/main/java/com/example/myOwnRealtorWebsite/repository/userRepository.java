package com.example.myOwnRealtorWebsite.repository;

import com.example.myOwnRealtorWebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByFullName(String fullName);

    boolean existsByFullName(String fullName);
}
