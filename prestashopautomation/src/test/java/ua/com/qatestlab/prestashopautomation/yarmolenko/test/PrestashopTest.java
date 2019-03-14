package ua.com.qatestlab.prestashopautomation.yarmolenko.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import ua.com.qatestlab.prestashopautomation.yarmolenko.Header;
import ua.com.qatestlab.prestashopautomation.yarmolenko.MainPage;
import ua.com.qatestlab.prestashopautomation.yarmolenko.ProductCards;
import ua.com.qatestlab.prestashopautomation.yarmolenko.SearchResultsPage;


import java.util.concurrent.TimeUnit;


public class PrestashopTest {

    private static WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "webdriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
    }

    @Test
    public void testContents() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        ProductCards productCards = new ProductCards(driver);
        Header header = new Header(driver);

        System.out.println("Валюта в шапке сайта :" + header.getSelectedCurrency());
        System.out.println("Валюта карточек товара:" + productCards.getCurrencyOfProductCardsPrice());
        System.out.println(productCards.getProductCardsPriceListWithDiscont());
        Assert.assertTrue("Несоответствие валют в шапке и на карточках",(header
                .getSelectedCurrency()).contains(productCards.getCurrencyOfProductCardsPrice()));
        System.out.println("Проверка на соответствие валют в шапке и на карточках товаров");

        System.out.println("-------------------------------------------------------------------------");

        header.setTheCurrencyOfPrices("USD");
        System.out.println("Валюта в шапке сайта :" + header.getSelectedCurrency());
        System.out.println("Установили валюту USD");

        System.out.println("-------------------------------------------------------------------------");

        SearchResultsPage searchResultsPage = mainPage.searchInCatalog("dress");
        System.out.println("Произвели поиск в каталоге товаров по слову \"dress\"");

        System.out.println("-------------------------------------------------------------------------");

        Assert.assertTrue("Страница \"Результаты поиска\" не содержит надпись \"Товаров: \"" + productCards
                .getProductCardsList().size(),driver.getPageSource().contains("Товаров: " + productCards
                .getProductCardsList().size()));
        System.out.println("Страница \"Результаты поиска\" содержит надпись \"Товаров: " + productCards
                .getProductCardsList().size() + "\" : " + driver.getPageSource().contains("Товаров: " + productCards
                .getProductCardsList().size()));
        System.out.println("Проверка, что страница \"Результаты поиска\" содержит надпись \"Товаров: x\","
                        + "\n где x - количество действительно найденных товаров");

        System.out.println("-------------------------------------------------------------------------");

        System.out.println("Валюта карточек товара:" + productCards.getCurrencyOfProductCardsPrice());
        System.out.println(productCards.getProductCardsPriceListWithDiscont());
        Assert.assertEquals("Валюта карточек товара не доллар", "USD", productCards
                .getCurrencyOfProductCardsPrice());
        System.out.println("Проверка, что цены товаров указаны в USD");

        System.out.println("-------------------------------------------------------------------------");

        searchResultsPage.setTheSortCondition("от высокой к низкой");
        System.out.println(productCards.getProductCardsPriceListWithDiscont());
        System.out.println("Сортировка товаров по цене от высокой к низкой");

        System.out.println("-------------------------------------------------------------------------");

        Assert.assertTrue("Сортировка товаров по цене от высокой к низкой без скидки не верна",productCards
                        .areSortedFromHighToLowPricesWithoutDiscount());
        System.out.println(productCards.getProductCardsPriceListWithoutDiscount());
        System.out.println("Проверка оортировки товаров по цене от высокой к низкой без скидки");

        System.out.println("-------------------------------------------------------------------------");

        System.out.println(productCards.infoAboutProductsWithDiscount());
        System.out.println("Проверка, что у товаров со скидкой указана скидка в процентах вместе "
                + "с ценой до и после скидки");

        System.out.println("-------------------------------------------------------------------------");

        Assert.assertTrue("Цена до и после скидки не совпадает с указанным размером скидки",productCards
                .checkingPricesConsideringDiscounts());
        System.out.println("Цена до и после скидки совпадает с указанным размером скидки"
                + " (если округлить\n до двух знаков после запятой):   " + productCards
                .checkingPricesConsideringDiscounts());

        System.out.println("-------------------------------------------------------------------------");

    }

    @After
    public void tearDown() {
        driver.quit();
    }
}