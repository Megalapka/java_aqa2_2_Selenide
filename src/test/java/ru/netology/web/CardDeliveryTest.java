package ru.netology.web;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeEach
    void setUP() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

//    @AfterEach
//    void tearDown() {
//        closeWindow();
//    }

    @Test
    void shouldAccessCardOrderWithoutChoosingDate() {

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();
        $("[data-test-id='name'] .input__control").setValue("Иванова-Петрова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));

    }

    @Test
    void shouldAccessCardOrderWithChoosingMinDate() {

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        String validDate = $("[data-test-id='date'] .input__control").getAttribute("defaultValue");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(validDate);

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));

    }

    @Test
    void shouldAccessCardOrderWithChoosingCityFromPopupMenu() {

        $("[data-test-id='city'] .input__control").setValue("Во");
        $x("//span[contains(text(),'Воронеж')]").click();

        $("[data-test-id='name'] .input__control").setValue("Петрова Анна-Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));

    }


    @Test
    void shouldAccessCardOrderForwardSevenDays() {

        LocalDate date = LocalDate.now().plusDays(7);
        String newDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(newDate);

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));

    }

    @Test
    void shouldChooseDateFromPopupCalendarEasyMode() {

        LocalDate date = LocalDate.now().plusDays(7);
        String newDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(newDate);

        $x("//span[contains(@class, 'icon_name_calendar')]").click();
        $(".calendar__day_state_current").click();

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));

    }

    @Test
    void shouldChooseDateFromPopupCalendarHardMode() {

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        String element1;
        String element2 = $(".calendar__day_state_today").getAttribute("className");
        $x("//span[contains(@class, 'icon_name_calendar')]").click();

        int trCount = 0;
        int tdCount = 0;

        for (int i = 2; i < 7; i++) {
            for (int j = 1; j < 8; j++) {
                element1 = $x("//tr[" + i + "]/td[" + j + "]").getAttribute("className");
                if (element1.equals(element2)) {
                    trCount = i + 1;
                    tdCount = j;
                    break;
                }
            }
        }

        if (trCount == 6) {
            $$(".calendar__arrow_direction_right").get(1).click();
            trCount = 2;
        }

        $x("//tr[" + trCount + "]/td[" + tdCount + "]").click();

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldChooseDateFromPopupCalendarHardModeExcludeHolidays() {

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        String element1;
        String element2 = $(".calendar__day_state_today").getAttribute("className");
        $x("//span[contains(@class, 'icon_name_calendar')]").click();

        int trCount = 0;
        int tdCount = 0;

        for (int i = 2; i < 7; i++) {
            for (int j = 1; j < 8; j++) {
                element1 = $x("//tr[" + i + "]/td[" + j + "]").getAttribute("className");
                if (element1.equals(element2)) {
                    trCount = i + 1;
                    tdCount = j;
                    break;
                }
            }
        }

        if (trCount == 6) {
            $$(".calendar__arrow_direction_right").get(1).click();
            trCount = 2;
            if (tdCount > 5) {
                tdCount = 1;
            }
        }
        $x("//tr[" + trCount + "]/td[" + tdCount + "]").click();

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));

    }


    //Not access tests
    //=========================================================================================================
    @Test
    void shouldNotAccessWithEmptyCityField() {

        String validDate = $("[data-test-id='date'] .input__control").getAttribute("defaultValue");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(validDate);

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();

        $x("//span[contains(@data-test-id, 'city')]//span[contains(text(), " +
                "'Поле обязательно для заполнения')] ").shouldBe(visible);

    }

    @Test
    void shouldNotAccessWithWrongCityField() {

        $("[data-test-id='city'] .input__control").setValue("Кипр");
        $(".button__text").click();

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();


        $x("//span[contains(@data-test-id, 'city')]//span[contains(text(), " +
                "'Доставка в выбранный город недоступна')] ").shouldBe(visible);

    }


    @Test
    void shouldNotAccessWithEmptyDateField() {
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.BACK_SPACE);

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();

        $x("//span[contains(@data-test-id, 'date')]//span[contains(text(), " +
                "'Неверно введена дата')] ").shouldBe(visible);

    }

    @Test
    void shouldNotAccessWithEmptyNameField() {
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();

        $x("//span[contains(@data-test-id, 'name')]//span[contains(text(), " +
                "'Поле обязательно для заполнения')] ").shouldBe();

    }

    @Test
    void shouldNotAccessWithWrongNameField() {
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='name'] .input__control").setValue("Mr. Smith");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();

        $x("//span[contains(@data-test-id, 'name')]//span[contains(text(), " +
                "'Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.')] ")
                .shouldBe(visible);

    }

    @Test
    void shouldNotAccessPhoneFieldPlusOneNumber() {
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+798765432102");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();

        $x("//span[contains(@data-test-id, 'phone')]//span[contains(text(), " +
                "'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.')] ")
                .shouldBe(visible);

    }

    @Test
    void shouldNotAccessPhoneFieldShortNumber() {
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+7902");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();

        $x("//span[contains(@data-test-id, 'phone')]//span[contains(text(), " +
                "'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.')] ")
                .shouldBe(visible);

    }

    @Test
    void shouldNotAccessPhoneFieldWithoutPlus() {
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("79012345678");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();

        $x("//span[contains(@data-test-id, 'phone')]//span[contains(text(), " +
                "'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.')] ")
                .shouldBe(visible);

    }


    @Test
    void shouldNotAccessPhoneFieldWithAnotherSymbols() {
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+7rtdf3%gq4r");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();

        $x("//span[contains(@data-test-id, 'phone')]//span[contains(text(), " +
                "'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.')] ")
                .shouldBe(visible);

    }

    @Test
    void shouldNotAccessWithoutCheckedAgreement() {

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();
        $("[data-test-id='name'] .input__control").setValue("Иванова-Петрова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");

        $(".button__text").click();
        $(".input_invalid[data-test-id='agreement']").shouldBe(visible);

    }


}
