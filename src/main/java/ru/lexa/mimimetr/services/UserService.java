package ru.lexa.mimimetr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lexa.mimimetr.model.Kitty;
import ru.lexa.mimimetr.model.Pair;
import ru.lexa.mimimetr.model.User;
import ru.lexa.mimimetr.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	public void save(User user) {
		userRepository.save(user);
	}

	public List<User> finAll() {
		return userRepository.findAll();
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	public void deletePair(Long uId, Long pId) {
		userRepository.deletePair(uId, pId);
	}

	public void addNewPairs(List<Pair> newPairs) {
		List<User> users = userRepository.findAll();

		for (User user : users) {
			user.getPairs().addAll(newPairs);
		}

		userRepository.saveAll(users);
	}

	public void addKitty(User user, Kitty kitty) {
		user.setKitty(kitty);
		userRepository.save(user);
	}
}
