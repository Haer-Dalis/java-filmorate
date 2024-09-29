package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testBadEmail() {
		User user = new User("badEmail",
				"ErevosX", "Alex", LocalDate.of(2010, 9, 7));
		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testLoginWithProbely() {
		User user = new User("good@email.com", "bad login", "Alex",
				LocalDate.of(2010, 9, 7));
		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testUser() {
		User user = new User("hero@email.com",
				"validLogin", "Alex", LocalDate.of(2010, 9, 7));
		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testReleaseDate() {
		Film film = new Film("Dragonslayer", "A fantasy movie",
				LocalDate.of(1767, 7, 5), 180);
		Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testBadBirthday() {
		User user = new User("good@email.com", "goodLogin", "Max",
				LocalDate.of(2094, 7, 5));
		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testDuration() {
		Film film = new Film("Dragonslayer", "A fantasy movie",
				LocalDate.of(1981, 6, 26), -10);
		Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testBadDescriptionh() {
		String description = "Dragonslayer is a 1981 American dark fantasy film directed by Matthew Robbins from a " +
				"screenplay he co-wrote with Hal Barwood. It stars Peter MacNicol, Ralph Richardson, John Hallam, and " +
				"Caitlin Clarke. It was a co-production between Paramount Pictures and Walt Disney Productions, where " +
				"Paramount handled North American distribution and Disney handled international distribution through " +
				"Buena Vista International. The story is set in a fictional medieval kingdom where a young wizard " +
				"encounters challenges as he hunts a dragon, Vermithrax Pejorative.";
		Film film = new Film("Dragonslayer", description,
				LocalDate.of(1981, 6, 26), 180);
		Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
		assertFalse(violations.isEmpty());
	}



}
