package com.github.mdeluise.pinboard;

import com.github.mdeluise.pinboard.page.Page;
import com.github.mdeluise.pinboard.page.body.PageBody;
import com.github.mdeluise.pinboard.scraper.PageScraper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * This should not be used, instead it should be used the "normal" PageScraper.
 * The problem with that is the following: the call Jsoup.connect(toUpdate.getUrl()).get() fails saying "Connection
 * refused" (it fails connection to any endpoint, so it seems to be something related to spring's permission of some
 * kind).
 * Thus, the proper way is to use the "normal" PageScraper, and to insert the url "http://localhost:{port}/dummy/" to
 * the testing pages.
 */
@Component
@Primary
public class DummyPageScraper extends PageScraper {
    @Override
    public Page fillMissingFields(final Page page) {
        Page filled = new Page(page);
        PageBody pageBody = new PageBody(filled);
        filled.setTitle("Filled title");
        filled.setHeaderImgUrl("<img src='https://foo.bar'/>");
        pageBody.setContent(String.format("<html>Page body of %s</html>", page.getId()));
        filled.setBody(pageBody);
        return filled;
    }
}
