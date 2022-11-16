package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
    LoginPage loginPage;
    VerificationPage verificationPage;
    VerificationCode verificationCode;
    DashboardPage dashboardPage;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
        verificationPage = loginPage.validLogin(DataHelper.getAuthInfo());
        verificationCode = DataHelper.getVerificationCodeFor(DataHelper.getAuthInfo());
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
    }

    @Test
    void shouldTransferFromFirstToSecond() {
        var amount = generateValidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(getSecondCardInfo());
        dashboardPage = transferPage.validTransfer(String.valueOf(amount), getFirstCardInfo());
        var actualFirstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        var actualSecondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
        assertEquals(firstCardBalance - amount, actualFirstCardBalance);
        assertEquals(secondCardBalance + amount, actualSecondCardBalance);
    }

    @Test
    void notTransferFromFirstToSecond() {
        var amount = generateInvalidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(getSecondCardInfo());
        transferPage.transfer(String.valueOf(amount), getFirstCardInfo());
        transferPage.seeErrorMessage("Выполнена попытка перевода суммы, превышающей баланс" + amount);
        var actualFirstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        var actualSecondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
        assertEquals(firstCardBalance, actualFirstCardBalance);
        assertEquals(secondCardBalance, actualSecondCardBalance);
    }

    @Test
    void shouldTransferFromSecondToFirst() {
        var amount = generateValidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(getFirstCardInfo());
        dashboardPage = transferPage.validTransfer(String.valueOf(amount), getSecondCardInfo());
        var actualFirstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        var actualSecondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
        assertEquals(firstCardBalance + amount, actualFirstCardBalance);
        assertEquals(secondCardBalance - amount, actualSecondCardBalance);
    }
}
