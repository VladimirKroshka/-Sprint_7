import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import resources.POJO.Order;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static resources.constant.Endpoints.*;

@RunWith(Parameterized.class)
public class OrderCreationTests {

    private Order order;
    private int track;

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }

    private String[] colors;

    public OrderCreationTests(String[] colors) {
        this.colors = colors;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
        order = new Order();
        order.setFirstName("Naruto");
        order.setLastName("Uchiha");
        order.setAddress("Konoha, 142 apt.");
        order.setMetroStation(4);
        order.setPhone("+7 800 355 35 35");
        order.setRentTime(5);
        order.setDeliveryDate("2020-06-06");
        order.setComment("Saske, come back to Konoha");
        order.setColor(colors);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с различными параметрами цвета")
    public void testCreateOrder() {
        createOrder(order);
    }

    @Step("Создание заказа")
    private void createOrder(Order order) {
        Response response = given()
                .contentType("application/json")
                .body(order)
                .when()
                .post(CREATE_ORDER);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());

        track = response.jsonPath().getInt("track");
    }
    //Ручка отмены заказа поломанная
    //При создании заказа мы получаем track
    //При отмене заказа с этим track, метод возвращает 400 "Недостаточно данных для поиска"
    //@After
    public void tearDown() {
        if (track > 0) {
            cancelOrder(track);
        }
    }

    @Step("Отмена заказа")
    private void cancelOrder(int track) {
        given()
                .contentType("application/json")
                .body("{\"track\": " + track + "}")
                .when()
                .put(CANCEL_ORDER)
                .then()
                .statusCode(200);
    }
}
