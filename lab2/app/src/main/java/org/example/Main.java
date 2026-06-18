package org.example;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import org.example.utils.StringProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        loadBuildPassport();
        logger.info("Программа запущена");

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите текст для обработки:");
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String result = StringProcessor.processString(input);
                logger.info("Результат (reverse): {}", result);
            }
        } catch (Exception e) {
            logger.error("Ошибка при вводе данных", e);
        }
    }

    private static void loadBuildPassport() {
        Properties props = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("build-passport.properties")) {
            if (is != null) {
                props.load(is);
                System.out.println("\n=== BUILD PASSPORT INFORMATION ===");
                props.forEach((k, v) -> System.out.println(k + ": " + v));
                System.out.println("==================================\n");
            }
        } catch (Exception e) {
            logger.error("Не удалось прочитать паспорт сборки", e);
        }
    }
}

