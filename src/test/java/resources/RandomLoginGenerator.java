package resources;

import java.util.UUID;

public class RandomLoginGenerator {

    public static String generateRandomLogin() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }
}
