package ua.com.qatestlab.prestashopautomation.yarmolenko;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SearchResultsPage {
    private WebDriver driver;

    /**
     * The selected condition for sorting goods
     */
    @FindBy(xpath = "//a[@class=\"select-title\"]")
    private WebElement selectedSortCondition;

    /**
     * Selection list of conditions for sorting goods
     */
    @FindBy(xpath = "//div[@class=\"dropdown-menu\"]//a")
    private List<WebElement> dropDawnListOfSortConditions;

    public SearchResultsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     * Method to get a list of selection conditions for sorting goods
     */
    private List<WebElement> getDropDawnListOfSortConditions() {
        selectedSortCondition.click();
        WebDriverWait wait = (new WebDriverWait(driver, 10));
        wait.until(ExpectedConditions.visibilityOfAllElements(dropDawnListOfSortConditions));
        return dropDawnListOfSortConditions;
    }

    /**
     * Method for seting condition for sorting goods
     */
    public void setTheSortCondition(String sortCondition) throws InterruptedException {

        for (WebElement we : getDropDawnListOfSortConditions()) {
            if (we.getText().contains(sortCondition)) {
                we.click();
                WebDriverWait wait = (new WebDriverWait(driver, 10));
                wait.until(ExpectedConditions.invisibilityOf(driver.findElement(ProductCards.productPrice)));
                wait.until(ExpectedConditions.visibilityOfAllElements(ProductCards.productCardsPriceList));
            }
        }

    }
}
