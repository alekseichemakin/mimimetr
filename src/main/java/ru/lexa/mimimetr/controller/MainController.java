package ru.lexa.mimimetr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.lexa.mimimetr.model.Kitty;
import ru.lexa.mimimetr.model.Pair;
import ru.lexa.mimimetr.model.Role;
import ru.lexa.mimimetr.model.User;
import ru.lexa.mimimetr.services.KittyService;
import ru.lexa.mimimetr.services.PairService;
import ru.lexa.mimimetr.services.UserService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {
	@Autowired
	KittyService kittyService;

	@Autowired
	UserService userService;

	@Autowired
	PairService pairService;

	@Value("${upload.path}")
	private String uploadPath;

	@GetMapping
	public String mainPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("user", user);
		return "main";
	}

	@GetMapping("/random")
	public String getRandom(Model model, @AuthenticationPrincipal User user) {
		Pair pair = kittyService.getRandomPair(userService.findByUsername(user.getUsername()).getPairs());

		if (pair == null) {
			return "redirect:/result/";
		}
		model.addAttribute("cat1", kittyService.searchById(pair.getaId()));
		model.addAttribute("cat2", kittyService.searchById(pair.getbId()));
		model.addAttribute("pairId", pair.getId());
		return "random";
	}

	@GetMapping("/result")
	public String result(Model model) {
		List<Kitty> sortedKits = kittyService.getSortedByRat();
		model.addAttribute("kits", sortedKits.size() >= 10 ?
				sortedKits.subList(0, 10) :
				sortedKits.subList(0, sortedKits.size()));
		return "top";
	}

	@PostMapping("/vote")
	public String vote(@RequestParam(name = "winner") Integer winner,
	                   @RequestParam(name = "loser") Integer loser,
	                   @RequestParam(name = "pairId") Integer pairId,
	                   @AuthenticationPrincipal User user) {
		userService.deletePair(user.getId(), pairId);
		kittyService.calculateRating(winner, loser);
		return "redirect:/random";
	}

	@PostMapping("/add")
	public String add(@AuthenticationPrincipal User user,
	                  @RequestParam("file") MultipartFile file,
	                  @RequestParam String name,
	                  Model model) {
		if (user.getKitty() != null && !user.getRoles().contains(Role.ADMIN)) {
			model.addAttribute("message", "You already have cat");
		} else if (file == null || name == null || name.isEmpty()) {
			model.addAttribute("message", "Name and file shouldn't be empty");
		} else if (kittyService.getKitForName(name) != null) {
			model.addAttribute("message", "Choose another name, cat with this name are exist");
		} else {
			try {
				addCat(file, name, user);
			} catch (IOException e) {
				model.addAttribute("message", "Error: upload file error");
			}
		}
		return "main";
	}

	private void addCat(MultipartFile file, String name, User user) throws IOException {
		Kitty kitty = new Kitty();
		String fileName;

		fileName = createFile(file);
		kitty.setPhoto(fileName);
		kitty.setName(name);
		kitty.setRating(1400);
		kittyService.save(kitty);
		pairService.addPairs(kitty.getId());
		userService.addKitty(user, kitty);
	}

	private String createFile(MultipartFile file) throws IOException {
		File uploadDir = new File(uploadPath);

		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		String uuidFile = UUID.randomUUID().toString();
		String fileName = uuidFile + "." + file.getOriginalFilename();
		file.transferTo(new File(uploadPath + "/" + fileName));
		return fileName;
	}

}
