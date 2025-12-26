package utilityclass;

import org.testng.annotations.DataProvider;

public class DataUtils {
	
	@DataProvider(name = "invalidRegistrationData")
    public static Object[][] getInvalidRegistrationData() {
        return new Object[][] {

            { "", "vel", "valid" + System.currentTimeMillis() + "@test.com", "Password123!", "firstName` is required" },

            { "Kadamba", "", "valid" + System.currentTimeMillis() + "@test.com", "Password123!", "lastName` is required" },

            { "Kadamba", "vel", "", "Password123!", "Email is invalid" },
            
            { "Kadamba", "vel", "valid" + System.currentTimeMillis() + "@test.com", "", "password` is required" },

            { "Kadamba", "vel", "valid" + System.currentTimeMillis() + "@test.com", "123", "is shorter than the minimum allowed length (7)" },
            { "Kadamba", "vel", "valid" + System.currentTimeMillis() + "@valid", "Password123$", "Email is invalid" }
        };
    }

}
