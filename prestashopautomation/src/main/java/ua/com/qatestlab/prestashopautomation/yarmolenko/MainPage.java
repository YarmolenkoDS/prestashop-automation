package ua.com.qatestlab.prestashopautomation.yarmolenko;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import java.util.List;

public class MainPage {

    private WebDriver driver;

    /**
     *
     * Поле ввода текста для поиска в каталоге
     */
    @FindBy(xpath = "//input [@name=\"s\"]")
    private WebElement inputSearchField;

    /**
     *
     * Кнопка для поиска в каталоге
     */
    @FindBy(xpath = "//button[@type=\"submit\"]")
    private WebElement searchButton;

    /**
     *
     * Конструктор
     */
    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     *
     * Метод для получения символа установленной валюты в шапке сайта
     */
    public SearchResultsPage searchInCatalog(String wordForSearch) {
        inputSearchField.sendKeys(wordForSearch);
        searchButton.click();
        return new SearchResultsPage(driver);
    }

}
