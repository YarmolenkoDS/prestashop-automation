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
     * Карточки товаров на странице
     */
    @FindBy(xpath = "//div[@class=\"thumbnail-container\"]")
    private List<WebElement> productCardsList;

    /**
     * Список цен карточек товаров на странице
     */
    @FindBy(xpath = "//span[@class=\"price\"]")
    static List<WebElement> productCardsPriceList;

    /**
     * Локатор для текущей цены товара
     */
    static By productPrice = By.xpath(".//span[@class=\"price\"]");

    /**
     * Локатор для азвания товара
     */
    private By productName = By.xpath(".//div[@class=\"product-description\"]//a");

    /**
     * Локатор для скидки на товар
     */
    private By discontOnProductCard = By.xpath(".//span[@class=\"discount-percentage\"]");

    /**
     * Локатор для цені товара без скидки
     */
    private By productPriceWithoutDiscont = By.xpath(".//span[@class=\"regular-price\"]");

    /**
     * Конструктор
     */
    public ProductCards(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     * Метод для получения списка карточек товаров на странице
     */
    public List<WebElement> getProductCardsList() {
        return productCardsList;
    }

    /**
     * Метод для получения символа валюты цены у карточек товаров
     */
    public String getCurrencyOfProductCardsPrice() {
        String result = "";
        char cuurencySymbol = ((driver.findElement(productPrice).getText())
                .substring((driver.findElement(productPrice).getText()).length() - 1)).charAt(0);
        for (WebElement we : getProductCardsList()) {
            String wePrice = we.findElement(productPrice).getText();
            if (!wePrice.substring(wePrice.length() - 1).equals("" + cuurencySymbol)) {
                throw new IllegalArgumentException("Валюта цен во всех карточках товаров должна быть одинаковой ");
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
     * Метод для проверка сортировки товаров по цене от высокой к низкой без скидки
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
     * Метод для проверки, что у товаров со скидкой указана скидка в процентах вместе с ценой до и после скидки
     */
    public String infoAboutProductsWithDiscount() {
        StringBuilder sb = new StringBuilder();
        for (WebElement we : getProductCardsList()) {

            if (we.getText().contains("%")) {
                sb.append("Товар:\"").append(we.findElement(productName).getText())
                        .append("\". Старая цена:").append(we.findElement(productPriceWithoutDiscont).getText())
                        .append(". Скидка:").append(we.findElement(discontOnProductCard).getText())
                        .append(". Новая цена:").append(we.findElement(productPrice).getText()).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Метод для проверки, что цена до и после скидки совпадает с указанным размером скидки
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
     * Метод для округления числа float до заданного количества знаков после запятой
     */
    private float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    /**
     * Метод для получения строки списка цен карточек товаров на странице с учётом скидки
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
     * Метод для получения строки списка цен карточек товаров на странице без скидки
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
