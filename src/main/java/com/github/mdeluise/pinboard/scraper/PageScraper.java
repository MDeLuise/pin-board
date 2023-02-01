package com.github.mdeluise.pinboard.scraper;

import com.github.mdeluise.pinboard.page.Page;
import com.github.mdeluise.pinboard.page.body.PageBody;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class PageScraper {

    public static Page fillMissingFields(final Page page) throws IOException {
        Page toUpdate = new Page(page);
        Document doc = Jsoup.connect(toUpdate.getUrl()).get();
        fillTitle(toUpdate, doc);
        fillHeaderImageUrl(toUpdate, doc);
        fillBody(toUpdate, doc);
        return toUpdate;
    }


    private static void fillHeaderImageUrl(Page toUpdate, Document doc) {
        Element firstImg = doc.select("body img").first();
        if (firstImg != null) {
            toUpdate.setHeaderImgUrl(firstImg.attr("src"));
        }
    }


    private static void fillTitle(Page toUpdate, Document doc) {
        String title = doc.title();
        if (Strings.isNotBlank(title)) {
            toUpdate.setTitle(doc.title());
        } else {
            toUpdate.setTitle(toUpdate.getUrl());
        }
    }


    private static void fillBody(Page toUpdate, Document doc) {
        PageBody pageBody = new PageBody(toUpdate);
        pageBody.setContent(doc.body().toString());
        toUpdate.setBody(pageBody);
    }
}
