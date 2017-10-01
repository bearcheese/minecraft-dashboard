package hu.bearmaster.minecraftstarter.dashboard.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.bearmaster.minecraftstarter.dashboard.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
