package org.terraO.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;
import org.terraO.model.Article;
import org.terraO.model.SearchBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResearchService {

    public List<Article> search() {
        return makeScrapping();
    }

    private List<Article> makeScrapping() {
        try {
            String pageNum = "1";
            String lang = "All";
            String area = "All";
            String URL = "https://appliedsciences.nasa.gov/join-mission/training?program_area=" + area + "&languages=" + lang + "&source=All&page=" + pageNum;

            WebClient client = new WebClient();
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);

            HtmlPage page = client.getPage(URL);
            List<HtmlElement> items = page.getByXPath("//div[@class='ds-list layout-wrapper']");
            List<Article> articles = new ArrayList<>();
            if (!items.isEmpty()) {
                // or HtmlDivision
                for (HtmlElement item : items) {
                    HtmlElement itemText = item.getFirstByXPath(".//div[@class='field field--name-node-title field--type-ds field--label-hidden field__item']//h2//a");
                    HtmlImage itemImage = item.getFirstByXPath(".//div[@class='field field--name-field-featured-image field--type-entity-reference field--label-hidden field__item']/img");
                    HtmlAnchor itemLink = item.getFirstByXPath(".//div[@class='field field--name-node-title field--type-ds field--label-hidden field__item']//h2//a");
                    String itemLinkA = itemLink == null ? "NoLinkFound" : "https://appliedsciences.nasa.gov/" + itemLink.getHrefAttribute() ;
                    assert itemLink != null;
                    Article article = new Article(itemText.asNormalizedText(), itemImage.getSrc(), itemLinkA);
                    articles.add(article);
                }
            }

            return articles;
        } catch (IOException e) {
            System.out.println(e);
        }

        return null;
    }
}
