package security;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
/**
 * 
 * @author Reshma Md
 *
 */
@Component
public class SecurityUtility {

	private static final String MATERIAL = "material"; // material should be protected carefully
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(MATERIAL.getBytes()));
	}
	
	@Bean
	public static String randomPassword() {
		String MATERIALCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder material = new StringBuilder();
		Random rnd = new Random();
		
		while(material.length() < 18) {
			int index = (int) (rnd.nextFloat()*MATERIALCHARS.length());
			material.append(MATERIALCHARS.charAt(index));
		}
		String materialStr = material.toString();
		return materialStr;
	}
}
