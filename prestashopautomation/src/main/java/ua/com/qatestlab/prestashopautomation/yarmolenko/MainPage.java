package ua.com.qatestlab.prestashopautomation.yarmolenko;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import java.util.List;

public class MainPage {

    private WebDriver driver;

    /**
     * The field for entering text to search in the catalog of goods
     */
    @FindBy(xpath = "//input [@name=\"s\"]")
    private WebElement inputSearchField;

    /**
     * Button to search the catalog of goods
     */
    @FindBy(xpath = "//button[@type=\"submit\"]")
    private WebElement searchButton;

    /**
     * Constructor
     */
    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     * Method to get the symbol of the installed currency in the header of the site
     */
    public SearchResultsPage searchInCatalog(String wordForSearch) {
        inputSearchField.sendKeys(wordForSearch);
        searchButton.click();
        return new SearchResultsPage(driver);
    }

}
