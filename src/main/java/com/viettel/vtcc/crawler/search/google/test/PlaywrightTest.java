package com.viettel.vtcc.crawler.search.google.test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.nio.file.Paths;

public class PlaywrightTest {
    private static Playwright playwright;
    private static Browser browser;

    public static void main(String[] args) {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions());
        Page page;
        String url = "https://www.google.com/search?q=bộ trưởng bộ quốc phòng việt nam&near=Cau Giay";

        page = browser.newPage();
        page.navigate(url);

        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("screenshot.png"))
                .setFullPage(true));
        page.close();
        browser.close();
        playwright.close();

    }
}
