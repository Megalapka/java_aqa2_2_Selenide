package ru.netology.web;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {


    public String forwardForDays(int days) // метод для увеличения даты на "days" дней.
    {
        LocalDate date = LocalDate.now();
        date = date.plusDays(days);

        int year = date.getYear();

        String corrMonth;
        if (date.getMonthValue() < 10) {
            corrMonth = "0" + date.getMonthValue();
        } else {
            corrMonth = String.valueOf(date.getMonthValue());
        }

        String corrDay;
        if (date.getDayOfMonth() < 10) {
            corrDay = "0" + date.getDayOfMonth();
        } else {
            corrDay = String.valueOf(date.getDayOfMonth());
        }

        String newDate = corrDay + "." + corrMonth + "." + year;

        return newDate;
    }


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
    void shouldAccessCardOrderWithChoosingDate() {

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

        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $(".menu-item__control").click();

        String dateOfSevenDays = forwardForDays(7);
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(dateOfSevenDays);

        $("[data-test-id='name'] .input__control").setValue("Иванова Ульяна Владимировна");
        $("[data-test-id='phone'] .input__control").setValue("+79876543210");
        $("[data-test-id='agreement']").click();

        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));

    }
}
