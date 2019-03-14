package ua.com.qatestlab.prestashopautomation.yarmolenko;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ProductCards {

    private WebDriver driver;
    /**
     * Cards of goods on the page
     */
    @FindBy(xpath = "//div[@class=\"thumbnail-container\"]")
    private List<WebElement> productCardsList;

    /**
     * List of prices from product cards per page
     */
    @FindBy(xpath = "//span[@class=\"price\"]")
    static List<WebElement> productCardsPriceList;

    /**
     * Locator by current  price of product
     */
    static By productPrice = By.xpath(".//span[@class=\"price\"]");

    /**
     * Locator by product name
     */
    private By productName = By.xpath(".//div[@class=\"product-description\"]//a");

    /**
     * Locator by product discont
     */
    private By discontOnProductCard = By.xpath(".//span[@class=\"discount-percentage\"]");

    /**
     * Locator by product price without discont
     */
    private By productPriceWithoutDiscont = By.xpath(".//span[@class=\"regular-price\"]");

    /**
     * Constructor
     */
    public ProductCards(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     * Method to get a list of product cards on the page
     */
    public List<WebElement> getProductCardsList() {
        return productCardsList;
    }

    /**
     * Method to get the currency symbol of price cards
     */
    public String getCurrencyOfProductCardsPrice() {
        String result = "";
        char cuurencySymbol = ((driver.findElement(productPrice).getText())
                .substring((driver.findElement(productPrice).getText()).length() - 1)).charAt(0);
        for (WebElement we : getProductCardsList()) {
            String wePrice = we.findElement(productPrice).getText();
            if (!wePrice.substring(wePrice.length() - 1).equals("" + cuurencySymbol)) {
                throw new IllegalArgumentException("The price currency in all commodity cards must be the same ");
            }
        }
            switch (cuurencySymbol) {
                case '₴':
                    result = "UAH";
                    break;
                case '$':
                    result = "USD";
                    break;
                case '€':
                    result = "EUR";
                    break;
            }
        return result;
    }

    /**
     * Method for checking the sorting of goods by price from high to low without discount
     */
    public boolean areSortedFromHighToLowPricesWithoutDiscount() {
        boolean flag = false;
        float[] arrOfpriceWithoutDiscont = new float[getProductCardsList().size()];
        int i = 0;
        for (WebElement we : getProductCardsList()) {
            String wePrice;
            if (!we.getText().contains("%")) {
                wePrice = we.findElement(productPrice).getText();
            } else {
                wePrice = we.findElement(productPriceWithoutDiscont).getText();
            }
            arrOfpriceWithoutDiscont[i] = Float.parseFloat(wePrice.substring(0,wePrice.length() - 2)
                    .replace(",","."));
            i++;
        }
        for (int j = 0; j < arrOfpriceWithoutDiscont.length-1; j++ ) {
            if (arrOfpriceWithoutDiscont[j] >= arrOfpriceWithoutDiscont[j + 1]) {
                flag = true;
            } else {
                return false;
            }
        }
        return flag;
    }

    /**
     *
     * Method to check that the discount items have a percentage discount with the price before and after the discount
     */
    public String infoAboutProductsWithDiscount() {
        StringBuilder sb = new StringBuilder();
        for (WebElement we : getProductCardsList()) {

            if (we.getText().contains("%")) {
                sb.append("Product:\"").append(we.findElement(productName).getText())
                        .append("\". Old price:").append(we.findElement(productPriceWithoutDiscont).getText())
                        .append(". Discont:").append(we.findElement(discontOnProductCard).getText())
                        .append(". New price:").append(we.findElement(productPrice).getText()).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Method to verify that the price before and after the discount matches the specified discount size
     */
    public boolean checkingPricesConsideringDiscounts() {
        boolean flag = true;
        for (WebElement we : getProductCardsList()) {
            if (we.getText().contains("%")) {
                String wePrice = we.findElement(productPrice).getText();
                String wePriceWithoutDiscont = we.findElement(productPriceWithoutDiscont).getText();
                String weDiscont = we.findElement(discontOnProductCard).getText();

                float wPrice = Float.parseFloat(wePrice.substring(0,wePrice.length() - 2)
                        .replace(",","."));
                float wPriceWithoutDiscont = Float.parseFloat(wePriceWithoutDiscont.substring(0,wePriceWithoutDiscont
                        .length() - 2).replace(",","."));
                float wDiscont = Float.parseFloat(weDiscont.substring(1,weDiscont.length() - 1));

                float rez = wPriceWithoutDiscont - wPriceWithoutDiscont/100f*wDiscont;

                if (round(rez,2) != wPrice) {
                    //System.out.println(rez + " -> " + round(rez,2) + " = " + wPrice);
                    flag = false;
                //} else {
                    //System.out.println(rez + " -> " + round(rez,2) + " = " + wPrice);
                }
            }
        }
        return flag;
    }

    /**
     * Method for rounding the number of float to a specified number of decimal places
     */
    private float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    /**
     * Method for getting a string of the list of prices for product cards on the page with discount
     */
    public String getProductCardsPriceListWithDiscont() {
        StringBuilder sb = new StringBuilder();
        for (WebElement we : getProductCardsList()) {
            sb.append(we.findElement(productPrice).getText()).append("; ");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Method for getting a string of the list of prices of products on the page without discount
     */
    public String getProductCardsPriceListWithoutDiscount() {
        StringBuilder sb = new StringBuilder();
        for (WebElement we : getProductCardsList()) {
            String wePrice;
            if (!we.getText().contains("%")) {
                wePrice = we.findElement(productPrice).getText();
            } else {
                wePrice = we.findElement(productPriceWithoutDiscont).getText();
            }
            sb.append(wePrice).append("; ");
        }
        sb.append("\n");
        return sb.toString();
    }

}
