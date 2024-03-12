package za.co.example.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.example.persistance.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByFirstNameIgnoreCase(String firstName);

    List<User> findByLastNameIgnoreCase(String lastName);

    User findByRsaId(String rsaId);

    List<User> findByDateOfBirth(LocalDate dateOfBirth);

    List<User> findByAddressId(String address);
}
