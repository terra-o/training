package org.terraO.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;
import org.terraO.model.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ResearchService {

    public List<Article> search() {
        return makeScrapping("", "All", "1", "All");
    }

    public List<Article> search(String searchContent, String searchTopic, String pageNum, String lang) {
        return makeScrapping(searchContent, searchTopic, pageNum, lang);
    }

    private List<Article> makeScrapping(String searchContent, String searchTopic, String pageNum, String lang) {
        HtmlPage page = createPage(searchContent, searchTopic, pageNum, lang);
        assert page != null;
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
    }


    private HtmlPage createPage (String searchContent, String searchTopic, String pageNum, String lang) {
        try {
            String URL;
            if(!Objects.equals(searchTopic, "All")) {
                searchTopic = getSearchTopic(searchTopic);
            }
            if (Objects.equals(searchContent, "")) {
                URL = "https://appliedsciences.nasa.gov/join-mission/training?program_area=" + searchTopic + "&languages=" + lang + "&source=All&page=" + pageNum;
            } else {
                URL = "https://appliedsciences.nasa.gov/join-mission/training?title=" + searchContent + "program_area=" + searchTopic + "&languages=" + lang + "&source=All&page=" + pageNum;
            }
            WebClient client = new WebClient();
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            return client.getPage(URL);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    private String getSearchTopic(String searchTopic) {
        searchTopic = searchTopic.toLowerCase();
        return switch (searchTopic) {
            case "applied sciences" -> "138";
            case "urban development" -> "348";
            case "develop" -> "153";
            case "wildfires" -> "152";
            case "climate" -> "147";
            case "risk & resilience" -> "143";
            case "sdg" -> "149";
            case "socioeconomic assessments" -> "151";
            case "valuables" -> "150";
            case "capacity building" -> "13";
            case "disasters" -> "14";
            case "ecological forecasting" -> "15";
            case "agriculture" -> "16";
            case "health & air quality" -> "17";
            case "water resources" -> "18";
            case "group on earth observations" -> "105";
            case "prizes & challenges" -> "141";
            default -> searchTopic;
        };
    }
}
