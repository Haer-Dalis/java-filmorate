package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friends.FriendshipStorageDAO;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingDAO;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, MpaRatingDAO.class, GenreStorage.class, LikesStorage.class, UserDbStorage.class, FriendshipStorageDAO.class})
public class FilmorateApplicationTests {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private FilmDbStorage filmStorage;

	@Autowired
	private UserDbStorage userStorage;

	@MockBean
	private GenreStorage genreStorage;

	@MockBean
	private LikesStorage likesStorage;

	@MockBean
	private MpaRatingDAO mpaStorage;

	@Autowired
	private FriendshipStorageDAO friendshipStorage;


	@BeforeEach
	public void setUp() {
		// Очищаем таблицы перед каждым тестом
		jdbcTemplate.update("DELETE FROM likes");
		jdbcTemplate.update("DELETE FROM film_genres");
		jdbcTemplate.update("DELETE FROM genres");
		jdbcTemplate.update("DELETE FROM films");
		jdbcTemplate.update("DELETE FROM mpa_rating");
		jdbcTemplate.update("DELETE FROM users");

		// Заполнение таблицы пользователей
		jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) VALUES ('Dunkan@yahoo.com', " +
				"'slayerXXX', 'Ivan Petrov', '1990-01-01')");
		jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) VALUES ('destroyer@yandex.ru', " +
				"'destroyerOfWorlds', 'Gena Ivanov', '1985-05-15')");
		jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) VALUES ('kapitolijskayaninja@gmail.com', " +
				"'ninjaSKapitolija', 'Natalia Sidorova', '2000-10-30')");

		// Заполнение тестовой базы данных
		jdbcTemplate.update("INSERT INTO mpa_rating (id, rating_name) VALUES (1, 'G'), (2, 'PG')");
		jdbcTemplate.update("INSERT INTO films (film_id, name, description, release_date, duration, mpa_rating) " +
				"VALUES (1, 'Москва слезам не верит', 'Классика советского кинематографа', '1980-02-11', 150, 1)");
		jdbcTemplate.update("INSERT INTO films (film_id, name, description, release_date, duration, mpa_rating) " +
				"VALUES (2, 'Ирония судьбы', 'Новогодний фильм для всей семьи', '1975-12-31', 184, 2)");
	}

	@Test
	public void shouldGetFilmById() {
		Film film = filmStorage.getFilmById(1);

		assertThat(film).isNotNull();
		assertThat(film.getName()).isEqualTo("Москва слезам не верит");
		assertThat(film.getDescription()).isEqualTo("Классика советского кинематографа");
		assertThat(film.getDuration()).isEqualTo(150);
		assertThat(film.getMpaRating().getId()).isEqualTo(1);
	}

	@Test
	public void shouldGetAllFilms() {
		List<Film> films = filmStorage.getFilms();

		assertThat(films).hasSize(2);
		assertThat(films.get(0).getName()).isEqualTo("Москва слезам не верит");
		assertThat(films.get(1).getName()).isEqualTo("Ирония судьбы");
	}

	@Test
	public void shouldGetPopularFilms() {
		List<Film> popularFilms = filmStorage.getPopularFilms(2);

		assertThat(popularFilms).hasSize(2);
		assertThat(popularFilms.get(0).getName()).isEqualTo("Ирония судьбы");
		assertThat(popularFilms.get(1).getName()).isEqualTo("Москва слезам не верит");
	}

	@Test
	public void shouldGetUserById() {
		User user = userStorage.getUserById(1);

		assertThat(user).isNotNull();
		assertThat(user.getName()).isEqualTo("Ivan Petrov");
		assertThat(user.getEmail()).isEqualTo("Dunkan@yahoo.com");
	}

	@Test
	public void shouldAddFriend() {
		Integer userId1 = jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE email = 'Dunkan@yahoo.com'",
				Integer.class);
		Integer userId2 = jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE email = 'destroyer@yandex.ru'",
				Integer.class);

		friendshipStorage.addFriend(userId1, userId2);

		Set<Integer> friends = friendshipStorage.getUserFriends(userId1);
		assertThat(friends).hasSize(1);
		assertThat(friends).contains(userId2);
	}

	@Test
	public void shouldDeleteFriend() {
		Integer userId1 = jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE email = 'Dunkan@yahoo.com'",
				Integer.class);
		Integer userId2 = jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE email = 'destroyer@yandex.ru'",
				Integer.class);
		friendshipStorage.addFriend(userId1, userId2);
		friendshipStorage.deleteFriend(userId1, userId2);
		Set<Integer> friends = friendshipStorage.getUserFriends(userId1);
		assertThat(friends).doesNotContain(2);
	}


}