package ru.lexa.mimimetr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.lexa.mimimetr.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	@Modifying
	@Query(value = "delete from usr_pairs where user_id=? and pairs_id=?" ,nativeQuery = true)
	void deletePair(long uId, long pId);
}
