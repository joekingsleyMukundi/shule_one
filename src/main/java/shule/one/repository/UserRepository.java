package shule.one.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import shule.one.entity.User;


public interface UserRepository extends CrudRepository<User, Long> {
	
	  Optional<User> findByUsername(String username);

	  Boolean existsByUsername(String username);

	  Boolean existsByEmail(String email);

}
