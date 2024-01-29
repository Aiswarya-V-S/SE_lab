import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class UserRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRegistrationApplication.class, args);
    }
}

@RestController
@RequestMapping("/api")
class UserController {

    private Map<String, User> userStorage = new HashMap<>();
    private Map<String, String> mobileOtpStorage = new HashMap<>();
    private Map<String, String> emailOtpStorage = new HashMap<>();

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Check if username is already taken
        if (userStorage.containsKey(user.getUsername())) {
            return "Username already exists";
        }

        // Generate and store OTP for mobile number
        String mobileOtp = generateOtp(); 
        mobileOtpStorage.put(user.getMobile(), mobileOtp);

        // Generate and store OTP for email
        String emailOtp = generateOtp();
        emailOtpStorage.put(user.getEmail(), emailOtp);

        // Save user data
        userStorage.put(user.getUsername(), user);

        return "User registered successfully. Mobile OTP: " + mobileOtp + ", Email OTP: " + emailOtp;
    }

    @PostMapping("/verifyMobileOtp")
    public String verifyMobileOtp(@RequestBody VerificationRequest verificationRequest) {
        String storedOtp = mobileOtpStorage.get(verificationRequest.getIdentifier());

        if (storedOtp != null && storedOtp.equals(verificationRequest.getOtp())) {
            return "Mobile OTP verified successfully";
        } else {
            return "Invalid Mobile OTP";
        }
    }

    @PostMapping("/verifyEmailOtp")
    public String verifyEmailOtp(@RequestBody VerificationRequest verificationRequest) {
        String storedOtp = emailOtpStorage.get(verificationRequest.getIdentifier());

        if (storedOtp != null && storedOtp.equals(verificationRequest.getOtp())) {
            return "Email OTP verified successfully";
        } else {
            return "Invalid Email OTP";
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }
}

class User {
    private String photo;
    private String name;
    private String username;
    private String mobile;
    private String password;
    private String email;

    // getters and setters

    // ... other properties and methods
}

class VerificationRequest {
    private String identifier;
    private String otp;

    // getters and setters
}