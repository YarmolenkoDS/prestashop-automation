package ua.com.qatestlab.prestashopautomation.yarmolenko;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Header {
    private WebDriver driver;

    /**
     *
     * Установленная валюта в шапке сайта
     */
    @FindBy(xpath = "//span[@class=\"expand-more _gray-darker hidden-sm-down\"]")
    private WebElement selectedCurrency;

    /**
     *
     * Список выбора валюты в шапке сайта
     */
    @FindBy(xpath = "//div[@class=\"currency-selector dropdown js-dropdown open\"]//a[@class=\"dropdown-item\"]")
    private List<WebElement> dropDawnListOfCurrency;

    /**
     *
     * Конструктор
     */
    public Header(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     *
     * Метод для получения символа установленной валюты в шапке сайта
     */
    public String getSelectedCurrency() {
        return selectedCurrency.getText();
    }

    /**
     *
     * Метод для получения списка выбора валюты в шапке сайта
     */
    private List<WebElement> getDropDawnListOfCurrency() {
        selectedCurrency.click();
        WebDriverWait wait = (new WebDriverWait(driver, 10));
        wait.until(ExpectedConditions.visibilityOfAllElements(dropDawnListOfCurrency));
        return dropDawnListOfCurrency;
    }

    /**
     *
     * Метод для установки валюты в шапке в шапке сайта
     */
    public void setTheCurrencyOfPrices(String currency) {
        for (WebElement we : getDropDawnListOfCurrency()) {
            if (we.getText().contains(currency)) {
                we.click();
            }
        }
    }
}
