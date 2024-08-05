package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Представляет заказ, который включает информацию о клиенте, автомобиле и статусе заказа.
 * <p>
 * Этот класс содержит данные о заказе, включая идентификатор заказа, клиента, автомобиль, дату заказа и статус.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    /**
     * Уникальный идентификатор заказа.
     */
    private int id;

    /**
     * Клиент, который сделал заказ.
     */
    private User client;

    /**
     * Автомобиль, который был заказан.
     */
    private Car car;

    /**
     * Дата создания заказа.
     */
    private LocalDate orderDate;

    /**
     * Статус заказа.
     */
    private String status;

    /**
     * Конструктор для создания объекта заказа с минимальным набором данных.
     * <p>
     * Этот конструктор используется, когда необходимо создать объект заказа, указывая только идентификатор и статус заказа.
     * </p>
     *
     * @param id     Уникальный идентификатор заказа.
     * @param status Статус заказа.
     */
    public Order(int id, String status) {
        this.id = id;
        this.status = status;
    }
}
