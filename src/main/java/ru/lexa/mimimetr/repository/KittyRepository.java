package ru.lexa.mimimetr.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.lexa.mimimetr.model.Kitty;

import java.util.List;

public interface KittyRepository extends CrudRepository<Kitty, Integer> {
	Kitty searchById(Integer id);
	List<Kitty> findAll();
	Kitty searchKittyByName(String name);

	@Query(value = "select id from kitty", nativeQuery = true)
	List<Integer> getAllIds();
}
