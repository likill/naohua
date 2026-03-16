package com.jiaxin.aiweb.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScreenShotUtilsTest {

    @Test
    void saveWebPageScreenshot() {
        String webPageScreenshot = WebScreenShotUtils.saveWebPageScreenshot("https://www.baidu.com");
        Assertions.assertNotNull(webPageScreenshot);
    }
}