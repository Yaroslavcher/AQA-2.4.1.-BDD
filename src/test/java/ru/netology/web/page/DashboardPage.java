package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement header = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceBeginning = ", баланс: ";
    private final String balanceEnd = " р.";


    public DashboardPage() {
        header.shouldBe(visible);
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        cards.findBy(attribute("data-test-id", cardInfo.getTestId())).$("button").click(); //Метод нажимает на кнопку Пополнить у нужной карты
        return new TransferPage();
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var text = cards.findBy(text(cardInfo.getCardNumber().substring(12, 16))).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val beginning = text.indexOf(balanceBeginning);
        val end = text.indexOf(balanceEnd);
        val value = text.substring(beginning + balanceBeginning.length(), end);
        return Integer.parseInt(value);
    }
}