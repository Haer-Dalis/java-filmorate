package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
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
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testLoginWithProbely() {
		User user = new User("good@email.com", "bad login", "Alex",
				LocalDate.of(2010, 9, 7));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testUser() {
		User user = new User("hero@email.com",
				"validLogin", "Alex", LocalDate.of(2010, 9, 7));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testBadBirthday() {
		User user = new User("good@email.com", "goodLogin", "Max",
				LocalDate.of(2094, 7, 5));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}


}
