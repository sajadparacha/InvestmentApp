package profitcalculation.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {
    
    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = PropertyLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.err.println("Sorry, unable to find " + fileName);
                return properties;
            }
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error loading properties from " + fileName + ": " + ex.getMessage());
        }
        return properties;
    }
    
    public static String getProperty(String fileName, String key, String defaultValue) {
        Properties properties = loadProperties(fileName);
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String fileName, String key, int defaultValue) {
        Properties properties = loadProperties(fileName);
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid integer value for " + key + " in " + fileName + ": " + value);
            }
        }
        return defaultValue;
    }
    
    public static double getDoubleProperty(String fileName, String key, double defaultValue) {
        Properties properties = loadProperties(fileName);
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid double value for " + key + " in " + fileName + ": " + value);
            }
        }
        return defaultValue;
    }
} 