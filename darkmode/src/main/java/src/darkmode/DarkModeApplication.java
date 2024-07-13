package src.darkmode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.util.concurrent.RateLimiter;

@SpringBootApplication
@RestController
public class DarkModeApplication {
	private static Boolean DARK_MODE_STATUS = false;
	private final RateLimiter rateLimiter = RateLimiter.create(1.0);

	public static void main(String[] args) {
		SpringApplication.run(DarkModeApplication.class, args);
	}

	@RequestMapping(path = "/dark-mode/toggle", method = RequestMethod.GET)
	public ResponseEntity<Void> toggleDarkMode() {
		if (rateLimiter.tryAcquire()) {
			DARK_MODE_STATUS = !DARK_MODE_STATUS;
			System.out.println("Toggled dark mode to: " + DARK_MODE_STATUS);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
		}
	}

	@RequestMapping(path = "/dark-mode", method = RequestMethod.GET)
	public ResponseEntity<Boolean> getDarkMode() {
		return ResponseEntity.ok(DARK_MODE_STATUS);
	}
}
