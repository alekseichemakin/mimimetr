package ru.lexa.mimimetr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.lexa.mimimetr.model.Pair;
import ru.lexa.mimimetr.model.Role;
import ru.lexa.mimimetr.model.User;
import ru.lexa.mimimetr.services.PairService;
import ru.lexa.mimimetr.services.UserService;

import java.util.Collections;
import java.util.List;

@Controller
public class RegistrationController {
	@Autowired
	private UserService userService;

	@Autowired
	PairService pairService;

	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}

	@PostMapping("/registration")
	public String addUser(User user, Model model) {
		User userFromDb = userService.findByUsername(user.getUsername());

		if (userFromDb != null) {
			model.addAttribute("message", "User exists!");
			return "registration";
		}

		user.setRoles(Collections.singleton(Role.USER));
		List<Pair> repPair = pairService.findAll();

		if (repPair != null)
			user.setPairs(repPair);
		userService.save(user);

		return "redirect:/login";
	}
}
