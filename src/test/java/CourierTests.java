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
import static resources.constant.Endpoints.*;

public class CourierTests {

    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
        courier = new Courier();
        //Запилил генератор рандомного логина, потому что слишком много фанатов Наруто,
        // и все ниндзя/хокаге/ниндзя2/ логины быть быть заняты ¯\_(ツ)_/¯
        //+ кейс теперь будет всегда отрабатывать
        courier.setLogin(RandomLoginGenerator.generateRandomLogin());
        courier.setPassword("1234");
        courier.setFirstName("Naruto");
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка создания курьера")
    public void testCreateCourier() {
        createCourier(courier);
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
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверка попытки создания курьера с уже существующим логином")
    public void testCreateDuplicateCourier() {
        createCourier(courier);
        createDuplicateCourier(courier);
    }

    @Step("Создание дублирующего курьера")
    private void createDuplicateCourier(Courier courier) {
        given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без обязательных полей")
    @Description("Проверка создания курьера без обязательных полей: только имя")
    public void testCreateCourierWithoutRequiredFieldsName() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setFirstName("Naruto");
        createCourierWithoutRequiredFields(incompleteCourier);
    }

    @Test
    @DisplayName("Создание курьера без обязательных полей")
    @Description("Проверка создания курьера без обязательных полей: только логин")
    public void testCreateCourierWithoutRequiredFieldsPassword() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setLogin(RandomLoginGenerator.generateRandomLogin());
        createCourierWithoutRequiredFields(incompleteCourier);
    }

    @Step("Создание курьера без обязательных полей")
    private void createCourierWithoutRequiredFields(Courier courier) {
        given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
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
