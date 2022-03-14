package ru.lexa.mimimetr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lexa.mimimetr.model.Kitty;
import ru.lexa.mimimetr.model.Pair;
import ru.lexa.mimimetr.repository.KittyRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class KittyService {
	@Autowired
	KittyRepository kittyRepository;

	public Pair getRandomPair(List<Pair> pairs) {
		if (pairs == null || pairs.isEmpty()) {
			return null;
		}
		int index = new Random().nextInt(pairs.size());

		return pairs.get(index);
	}

	public void calculateRating(Integer winner, Integer loser) {
		Kitty winKat = searchById(winner);
		Kitty losKat = searchById(loser);
		Double eA = getExpiredRating(winKat.getRating(), losKat.getRating());
		Double eB = getExpiredRating(losKat.getRating(), winKat.getRating());

		winKat.setRating(getNewRating(winKat.getRating(), 1, eA));
		losKat.setRating(getNewRating(losKat.getRating(), 0, eB));
		save(winKat);
		save(losKat);
	}

	//Ea = 1 / (1 + 10^((Rb-Ra)/400))
	private Double getExpiredRating(double rA, double rB) {
		return 1 / (1 + Math.pow(10, (rB - rA) / 400));
	}

	//Ra’ = Ra + K * (Sa — Ea)
	private double getNewRating(double rA, int sA, Double eA) {
		double newRat = rA + 400 * (sA - eA);
		if (newRat < 0) {
			newRat = 0;
		}
		return newRat;
	}

	public List<Kitty> getSortedByRat() {
		return findAll()
				.stream()
				.sorted((o1, o2) -> (int) (o2.getRating() - o1.getRating()))
				.collect(Collectors.toList());
	}

	@Transactional
	public void save(Kitty kitty) {
		kittyRepository.save(kitty);
	}

	@Transactional
	public Kitty getKitForName(String name) {
		return kittyRepository.searchKittyByName(name);
	}

	@Transactional
	public Kitty searchById(Integer id) {
		return kittyRepository.searchById(id);
	}

	@Transactional
	public List<Kitty> findAll() {
		return kittyRepository.findAll();
	}

	@Transactional
	public Kitty searchKittyByName(String name) {
		return kittyRepository.searchKittyByName(name);
	}

	@Transactional
	public List<Integer> getAllIds() {
		return kittyRepository.getAllIds();
	}
}
