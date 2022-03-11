package ru.lexa.mimimetr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lexa.mimimetr.model.Kitty;
import ru.lexa.mimimetr.model.Pair;
import ru.lexa.mimimetr.model.User;
import ru.lexa.mimimetr.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	public void addNewPairs(List<Pair> newPairs) {
		List<User> users = findAll();

		for (User user : users)
			user.getPairs().addAll(newPairs);

		saveAll(users);
	}

	public void addKitty(User user, Kitty kitty) {
		user.setKitty(kitty);
		save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return findByUsername(username);
	}

	@Transactional
	public void deletePair(long uId, long pId) {
		userRepository.deletePair(uId, pId);
	}

	@Transactional
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Transactional
	public void save(User user) {
		userRepository.save(user);
	}

	@Transactional
	public List<User> finAll() {
		return userRepository.findAll();
	}

	@Transactional
	public void saveAll(List<User> users) {
		userRepository.saveAll(users);
	}

	@Transactional
	public List<User> findAll() {
		return userRepository.findAll();
	}
}
