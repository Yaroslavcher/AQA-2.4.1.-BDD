package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
//    TransferPage transferPage = new TransferPage();

    @Test
    void shouldTransferFromFirstToSecond() {
/*        open("http://localhost:9999");
        var loginPage = new LoginPage();*/
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        var secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
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
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        var secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
        var amount = generateInvalidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(getSecondCardInfo());
        transferPage.Transfer(String.valueOf(amount), getFirstCardInfo());
        transferPage.seeErrorMessage("Выполнена попытка перевода суммы, превышающей баланс" + amount);
        var actualFirstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        var actualSecondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
        assertEquals(firstCardBalance, actualFirstCardBalance);
        assertEquals(secondCardBalance, actualSecondCardBalance);
    }

    @Test
    void shouldTransferFromSecondToFirst() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        var secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
        var amount = generateValidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(getFirstCardInfo());
        dashboardPage = transferPage.validTransfer(String.valueOf(amount), getSecondCardInfo());
        var actualFirstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        var actualSecondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
        assertEquals(firstCardBalance + amount, actualFirstCardBalance);
        assertEquals(secondCardBalance - amount, actualSecondCardBalance);
    }
}
