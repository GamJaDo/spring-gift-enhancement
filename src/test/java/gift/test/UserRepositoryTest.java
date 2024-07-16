package gift.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gift.model.User;
import gift.repository.UserRepository;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    private User user;
    
    @BeforeEach
    void setUp() {
    	user = new User("test@test.com", "pw");
    }

    @Test
    void save() {
        User actual = userRepository.save(user);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findByEmail() {
        userRepository.save(user);
        Optional<User> actual = userRepository.findByEmail(user.getEmail());
        assertThat(actual).isPresent();
        assertThat(actual.get().getEmail()).isEqualTo(user.getEmail());
    }
}