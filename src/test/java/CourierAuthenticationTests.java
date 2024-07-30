import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import resources.POJO.Courier;
import resources.RandomLoginGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static resources.constant.Endpoints.*;

public class CourierAuthenticationTests {

    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
        courier = new Courier();
        courier.setLogin(RandomLoginGenerator.generateRandomLogin());
        courier.setPassword("1234");
        courier.setFirstName("Naruto");
        createCourier(courier);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Проверка авторизации курьера")
    public void testCourierCanLogin() {
        loginCourier(courier);
    }

    @Test
    @DisplayName("Авторизация без обязательных полей")
    @Description("Проверка авторизации без обязательных полей: только пароль")
    public void testLoginWithoutRequiredFieldsLogin() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setPassword("1234");
        loginCourierWithoutRequiredFields(incompleteCourier);
    }

    @Test
    @DisplayName("Авторизация без обязательных полей")
    @Description("Проверка авторизации без обязательных полей: только логин")
    public void testLoginWithoutRequiredFieldsPassword() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setLogin(RandomLoginGenerator.generateRandomLogin());
        loginCourierWithoutRequiredFields(incompleteCourier);
    }

    @Step("Авторизация без обязательных полей")
    private void loginCourierWithoutRequiredFields(Courier courier) {
        given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при неправильном логине или пароле")
    @Description("Проверка ошибки при неправильном логине или пароле")
    public void testLoginWithWrongCredentials() {
        Courier wrongCourier = new Courier();
        wrongCourier.setLogin("wrong");
        wrongCourier.setPassword("wrong");
        loginWithWrongCredentials(wrongCourier);
    }

    @Step("Авторизация с неправильным логином или паролем")
    private void loginWithWrongCredentials(Courier courier) {
        given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Создание курьера")
    private void createCourier(Courier courier) {
        Response response = given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        // После создания курьера сразу авторизуемся и получаем его id
        loginCourier(courier);
    }

    @Step("Логин курьера")
    private void loginCourier(Courier courier) {
        Response response = given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post(LOGIN_COURIER);

        response.then()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = response.jsonPath().getInt("id");
    }

    @After
    public void tearDown() {
        deleteCourier(courierId);
    }

    @Step("Удаление курьера")
    private void deleteCourier(int courierId) {
        if (courierId > 0) {
            given()
                    .contentType("application/json")
                    .body("{\"id\": " + courierId + "}")
                    .when()
                    .delete(DELETE_COURIER + "/" + courierId)
                    .then()
                    .statusCode(200);
        }
    }
}
