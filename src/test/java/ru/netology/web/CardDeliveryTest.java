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

    @Test
    void shouldAccessCardOrderWithoutChoosingDate() {

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();
        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
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
    void shouldChooseDateFromPopupCalendar() {

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        $x("//span[contains(@class, 'icon_name_calendar')]").click();
        $(".calendar-input__calendar-wrapper").isDisplayed();


        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));

    }
}
