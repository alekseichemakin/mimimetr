package ru.lexa.mimimetr.repository;

import org.springframework.data.repository.CrudRepository;
import ru.lexa.mimimetr.model.Pair;

import java.util.List;

public interface PairRepository extends CrudRepository<Pair, Integer> {
	List<Pair> findAll();
}
