package ru.university;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

class Order {
    private String dishes;
    private double totalPrice;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime orderTime;

    public Order() {}

    public Order(String dishes, double totalPrice, int hour, int min) {
        this.dishes = dishes;
        this.totalPrice = totalPrice;
        this.orderTime = LocalTime.of(hour, min);
    }

    @JsonProperty("dishes")
    public String getDishes() { return dishes; }

    @JsonProperty("totalPrice")
    public double getTotalPrice() { return totalPrice; }

    @JsonProperty("orderTime")
    public LocalTime getOrderTime() { return orderTime; }

    @Override
    public String toString() {
        return "Блюда: " + dishes +
               ", Стоимость: " + totalPrice +
               ", Время: " + orderTime;
    }
}

public class Restaurant {
    private static final Logger logger = LoggerFactory.getLogger(Restaurant.class);
    
    private Map<Integer, Order> orders = new HashMap<>();

    public void addOrder(int tableNumber, Order order) {
        orders.put(tableNumber, order);
        logger.info("Заказ успешно добавлен для столика №{}", tableNumber);
    }

    public Order getOrder(int tableNumber) {
        Order order = orders.get(tableNumber);
        if (order == null) {
            logger.warn("Попытка получить заказ для столика №{}: заказ не найден", tableNumber);
        } else {
            logger.info("Заказ для столика №{} успешно извлечен", tableNumber);
        }
        return order;
    }

    public void removeOrder(int tableNumber) {
        if (orders.containsKey(tableNumber)) {
            orders.remove(tableNumber);
            logger.info("Заказ для столика №{} удалён из системы", tableNumber);
        } else {
            logger.error("Ошибка удаления: столик №{} не имеет активных заказов", tableNumber);
        }
    }

    public Map<Integer, Order> getOrders() {
        return Collections.unmodifiableMap(orders); 
    }

    @Override
    public String toString() {
        if (orders.isEmpty()) {
            return "Список заказов пуст.";
        }
        StringBuilder sb = new StringBuilder("Текущие заказы в системе:\n");
        orders.forEach((table, order) -> 
            sb.append("Столик №").append(table).append(" -> ").append(order).append("\n")
        );
        return sb.toString();
    }

    public static void main(String[] args) {
        logger.info("Приложение 'Ресторан' запускается...");
        
        Restaurant restaurant = new Restaurant();

        Order order1 = new Order("Паста Болоньезе, Капучино", 850.0, 9, 30);
        Order order2 = new Order("Стейк, Салат Цезарь, Сок", 1500.0, 10, 20);
        Order order3 = new Order("Суп, Компот", 400.0, 10, 30);

        restaurant.addOrder(1, order1);
        restaurant.addOrder(2, order2);
        restaurant.addOrder(3, order3);

        System.out.println("\n" + restaurant);

        try {
            logger.info("Начало процесса сериализации данных в JSON");
            String jsonOutput = JsonSerializer.serialize(restaurant);
            System.out.println(jsonOutput);
        } catch (Exception e) {
            logger.error("Критическая ошибка при работе с JSON", e);
        }

        restaurant.removeOrder(1);
        restaurant.getOrder(5);

        logger.info("Работа приложения завершена.");
    }
}