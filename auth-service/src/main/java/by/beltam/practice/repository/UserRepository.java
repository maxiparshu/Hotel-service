package by.beltam.practice.repository;

import by.beltam.practice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByName(String name);
    Optional<User> getUserByName(String name);

}
