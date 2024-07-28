package resources;

import java.util.UUID;

public class RandomLoginGenerator {

    public static String generateRandomLogin() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }

    public static void main(String[] args) {
        String randomLogin = generateRandomLogin();
    }
}