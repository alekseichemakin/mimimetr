package ru.lexa.mimimetr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lexa.mimimetr.model.Kitty;
import ru.lexa.mimimetr.model.Pair;
import ru.lexa.mimimetr.repository.PairRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class PairService {
	@Autowired
	PairRepository pairRepository;

	@Autowired
	KittyService kittyService;

	@Autowired
	UserService userService;

	public void addPairs(Integer id) {
		List<Integer> repoIds = kittyService.getAllIds();
		List<Pair> newPairs = new ArrayList<>();

		for (Integer repoId : repoIds)
			if (!repoId.equals(id))
				newPairs.add(new Pair(id, repoId));

		saveAll(newPairs);

		userService.addNewPairs(newPairs);
	}

	@PostConstruct
	public List<Pair> initPairs() {
		List<Pair> pairs = new ArrayList<>();
		List<Kitty> kitties = kittyService.findAll();
		List<Pair> repoPairs = findAll();

		for (int i = 0; i < kitties.size(); i++) {
			for (int j = i + 1; j < kitties.size(); j++) {
				int[] pair = new int[2];
				pair[0] = kitties.get(i).getId();
				pair[1] = kitties.get(j).getId();
				Pair p = new Pair(pair[0], pair[1]);
				if (!repoPairs.contains(p))
					pairs.add(p);
			}
		}

		saveAll(pairs);
		return pairs;
	}

	@Transactional
	public List<Pair> findAll() {
		return pairRepository.findAll();
	}

	@Transactional
	public void saveAll(List<Pair> pairs) {
		pairRepository.saveAll(pairs);
	}
}
