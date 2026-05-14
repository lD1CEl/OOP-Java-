package ru.nsu.ccfit.factory.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties properties;

    public Config(String fileName) {
        properties = new Properties();
        try (InputStream is = Config.class.getResourceAsStream("/" + fileName)) {
            if (is == null) {
                throw new RuntimeException("файл конфигурации не найден: " + fileName);
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("ошибка при чтении файла конфигурации", e);
        }
    }

    public int getInt(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("в конфиге нет параметра: " + key);
        }
        return Integer.parseInt(value.trim());
    }

    public boolean getBoolean(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("в конфиге нет параметра: " + key);
        }
        return Boolean.parseBoolean(value.trim());
    }
}
