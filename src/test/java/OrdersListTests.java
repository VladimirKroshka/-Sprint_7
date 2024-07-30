import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static resources.constant.Endpoints.*;


public class OrdersListTests {

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка получения списка всех заказов")
    public void testGetOrdersList() {
        getOrdersList();
    }

    //Шаг отрабатывает очень долго, порядка 30 секунд
    //А какая-то доп. фильтрация из спеки не отрабатывает (
    @Step("Получение списка заказов")
    private void getOrdersList() {
        Response response = given()
                .when()
                .get(GET_ORDERS);

        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
