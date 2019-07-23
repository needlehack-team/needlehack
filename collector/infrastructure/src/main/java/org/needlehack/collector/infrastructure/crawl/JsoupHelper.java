package org.needlehack.collector.infrastructure.crawl;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Utils for Jsoup library
 */
public class JsoupHelper {

    public static Document getJsoupDocument(final String url, final Charset cs,
                                            final String userAgent) throws HTTPException {

        Response response;
        Document doc = null;

        try {
            response = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .userAgent(userAgent)
                    .timeout(12000)
                    .followRedirects(true)
                    .execute();
            doc = response.parse();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc;
    }

    public static Document getJsoupDocumentFromInput(final String contentHtml) throws HTTPException {
        return Jsoup.parse(contentHtml);
    }

    /**
     * Utility to remove the HTML tags in the String
     *
     * @param inputToClean
     * @return
     */
    public static String cleanHtmlFromInput(String inputToClean) {
        return Jsoup.clean(inputToClean, Whitelist.basic());
    }
}
