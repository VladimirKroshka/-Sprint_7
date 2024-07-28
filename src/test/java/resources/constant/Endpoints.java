package resources.constant;

public class Endpoints {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru"; // Замените на реальный URL API
    public static final String CREATE_COURIER = "/api/v1/courier";
    public static final String LOGIN_COURIER = "/api/v1/courier/login";
    public static final String DELETE_COURIER = "/api/v1/courier/{id}";
    public static final String CREATE_ORDER = "/api/v1/orders";
    public static final String CANCEL_ORDER = "/api/v1/orders/cancel";
    public static final String GET_ORDERS = "/api/v1/orders";
}
