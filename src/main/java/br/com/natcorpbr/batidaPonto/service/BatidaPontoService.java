package br.com.natcorpbr.batidaPonto.service;

import br.com.natcorpbr.batidaPonto.logger.LoggerPonto;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
public class BatidaPontoService {

    //@Scheduled(cron = "0 0 8,12,13,17 * * MON-FRI")
    public void baterPonto(){


        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://www.natcorpbr.com.br/apex/rh/f?p=307:LOGIN_DESKTOP:12978665965806:::::&tz=-3:00");

            System.out.println("Url atual: " + driver.getCurrentUrl());

            WebElement selectElement = driver.findElement(By.id("P900_COD_EMPRESA"));
            Select select = new Select(selectElement);
            select.selectByValue("309");

            driver.findElement(By.id("P900_MATRICULA")).click();
            driver.findElement(By.id("P900_MATRICULA")).sendKeys("116");

            driver.findElement(By.id("P900_SENHA")).click();
            driver.findElement(By.id("P900_SENHA")).sendKeys("37734915817");

            driver.findElement(By.cssSelector("button.t-Button.t-Button--hot")).click();

            boolean loginSuccess = wait.until(ExpectedConditions.urlContains("f?p=307:1:"));
            if (!loginSuccess) {
                System.out.println("ERRO: Login Falhou!");
                return;
            }

            WebElement card = driver.findElement(By.xpath("//li[contains(@class, 't-Cards-item')]//h3[text()='Registro de Ponto']"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", card);

            //WebElement close = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.ui-button.ui-corner-all.ui-widget.ui-button-icon-only.ui-dialog-titlebar-close")));
            //close.click();

            String mensagem = null;
            try {
                WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("iframe")));
                driver.switchTo().frame(1);

                WebElement btnComprovante = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Marcar Ponto']]")));
                executor.executeScript("arguments[0].click();", btnComprovante);

                WebElement alertMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='t-Alert-title']")));
                mensagem = alertMessage.getText();

                if (mensagem.contains("Marcação Realizada com Sucesso!")) {
                    System.out.println("Mensagem Confirmada! " + mensagem);
                } else {
                    System.out.println("Mensagem diferente do esperado: " + mensagem);
                }


            } catch (NoSuchElementException e) {
                System.out.println("❌ ERRO: O botão ou a mensagem não foram encontrados.");
                return;
            } finally {
                driver.switchTo().defaultContent();
            }

            LoggerPonto.registraPonto(mensagem);

        } catch (TimeoutException e) {
            System.out.println("Erro: O botão não foi encontrado ou não está visível.");
            System.out.println("Erro durante o teste: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
            System.exit(0);
        }

    }
}
