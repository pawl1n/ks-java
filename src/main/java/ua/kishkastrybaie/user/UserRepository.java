package ua.kishkastrybaie.user;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailEqualsIgnoreCase(String email);
  Integer countAllByRoleAndCreatedAtBetween(Role role, Instant startDate, Instant endDate);
}
