package com.abien.webtest;

import java.net.MalformedURLException;
import static org.hamcrest.CoreMatchers.containsString;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author adam-bien.com
 */
@RunAsClient
@RunWith(Arquillian.class)
public class SearchIT {

    @Drone
    WebDriver page;

    @FindBy(id = "searchInput")
    WebElement input;

    @FindBy(className = "formBtn")
    WebElement button;

    @Test
    public void openAdminPage() throws MalformedURLException {
        page.get("http://www.wikipedia.org");
        String title = page.getTitle();
        System.out.println("title = " + title);
        assertThat(title, containsString("Wikipedia"));

        input.sendKeys("java");
        Graphene.guardHttp(button).click();

    }
}
