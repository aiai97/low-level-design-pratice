package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class ConfigManager {
    private final String databaseUrl;
    private final String databaseUsername;

    private final String redisHost;
    private final int redisPort;

    private final int paymentTimeout;
    private ConfigManager() {
        databaseUrl = "jdbc:postgresql://localhost:5432/shop";
        databaseUsername = "admin";

        redisHost = "localhost";
        redisPort = 6379;

        paymentTimeout = 5000;
    }

    private static final ConfigManager INSTANCE = new ConfigManager();


    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public int getPaymentTimeout() {
        return paymentTimeout;
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }
}

class Demo{
    public static void main(String[] args) {

        ConfigManager config1 = ConfigManager.getInstance();
        ConfigManager config2 = ConfigManager.getInstance();

        System.out.println(config1 == config2);
        System.out.println(config1);
        System.out.println(config2);
    }
}
// final class
// -> cannot be inherited or extended
//
// private constructor
// -> cannot be instantiated from outside the class
//
// final fields
// -> their references/values cannot be reassigned after initialization
//
// static final instance
// -> keep one shared instance
//
// public static getInstance()
// -> provide global access to that instance
//
// => Singleton