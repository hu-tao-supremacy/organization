package app.onepass.organizer.utilities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// FOR DEBUGGING PURPOSES
@ConfigurationProperties("spring.datasource")
@Component
public class EnvironmentProperties {
	public static String url;
	public static String username;
	public static String password;
}
