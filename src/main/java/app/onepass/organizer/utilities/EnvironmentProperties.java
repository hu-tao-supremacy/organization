package app.onepass.organizer.utilities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// DEBUG
@ConfigurationProperties("spring.datasource")
@Component
public class EnvironmentProperties {
	public static String username;
}
