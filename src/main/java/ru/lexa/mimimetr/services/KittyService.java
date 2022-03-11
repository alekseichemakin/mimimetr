package ru.lexa.mimimetr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lexa.mimimetr.model.Kitty;
import ru.lexa.mimimetr.model.Pair;
import ru.lexa.mimimetr.repository.KittyRepository;
import ru.lexa.mimimetr.repository.PairRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class KittyService {
	@Autowired
	KittyRepository kittyRepository;

	@Autowired
	PairRepository pairRepository;

	public Pair getRandomPair(List<Pair> pairs) {
		if (pairs == null || pairs.isEmpty())
			return null;
		int index = new Random().nextInt(pairs.size());
		return pairs.get(index);
	}

	public void calculateRating(Integer winner, Integer loser) {
		Kitty winKat = getKitForId(winner);
		Kitty losKat = getKitForId(loser);

		Double eA = getExpiredRating(winKat.getRating(), losKat.getRating());
		Double eB = getExpiredRating(losKat.getRating(), winKat.getRating());

		winKat.setRating(getNewRating(winKat.getRating(), 1, eA));
		losKat.setRating(getNewRating(losKat.getRating(), 0, eB));

		kittyRepository.save(winKat);
		kittyRepository.save(losKat);
	}

	//Ea = 1 / (1 + 10^((Rb-Ra)/400))
	private Double getExpiredRating(double rA, double rB) {
		return 1 / (1 + Math.pow(10, (rB - rA) / 400));
	}

	//Ra’ = Ra + K * (Sa — Ea)
	private double getNewRating(double rA, int sA, Double eA) {
		double newRat = rA + 400 * (sA - eA);
		if (newRat < 0)
			newRat = 0;
		return newRat;
	}

	public Kitty getKitForId(Integer id) {
		return kittyRepository.searchById(id);
	}

	public List<Kitty> getSortedByRat() {
		return kittyRepository.findAll()
				.stream()
				.sorted((o1, o2) -> (int) (o2.getRating() - o1.getRating()))
				.collect(Collectors.toList());
	}

	public void save(Kitty kitty) {
		kittyRepository.save(kitty);
	}

//	@PostConstruct
//	public List<Pair> initPairs() {
//		List<Pair> pairs = new ArrayList<>();
//		List<Kitty> kitties = kittyRepository.findAll();
//		List<Pair> repoPairs = pairRepository.findAll();
//
//		for (int i = 0; i < kitties.size(); i++) {
//			for (int j = i + 1; j < kitties.size(); j++) {
//				int[] pair = new int[2];
//				pair[0] = kitties.get(i).getId();
//				pair[1] = kitties.get(j).getId();
//				Pair p = new Pair(pair[0], pair[1]);
//				if (!repoPairs.contains(p))
//					pairs.add(p);
//
//			}
//		}
//
//		pairRepository.saveAll(pairs);
//		return pairs;
//	}

	public Kitty getKitForName(String name) {
		return kittyRepository.searchKittyByName(name);
	}
}
