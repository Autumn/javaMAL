import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 18/06/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class CharacterSearchResults {
    protected CharacterSearchResults searchResults;
    public CharacterSearchResults(String siteText) {
        scrapeSearchResults(siteText);
    }

    public void scrapeSearchResults(String siteText) {
        Document doc = Jsoup.parse(siteText);
        Elements rows = doc.select("table").get(1).select("tr");
        rows.remove(0);
        for (Element row : rows) {
            Elements cols = row.select("td");

            Element imageNode = cols.get(0);
            System.out.println(imageNode.select("a").attr("href"));
            System.out.println(imageNode.select("img").attr("src"));

            Element nameNode = cols.get(1);
            System.out.println(nameNode.select("a").attr("href"));
            System.out.println(nameNode.select("a").text());
            System.out.println(nameNode.select("small").text());

            Elements titlesNodes = cols.get(2).select("a");
            for (Element link : titlesNodes) {
                Pattern animePattern = Pattern.compile("http://myanimelist.net/anime/(\\d+)/.*");
                Pattern mangaPattern = Pattern.compile("http://myanimelist.net/manga/(\\d+)/.*");
                Matcher animeMatch = animePattern.matcher(link.attr("href"));
                Matcher mangaMatch = mangaPattern.matcher(link.attr("href"));
                if (animeMatch.find()) {
                    System.out.println("anime " + link.attr("href") + " " + link.text());
                } else if (mangaMatch.find()) {
                    System.out.println("manga " + link.attr("href") + " " + link.text());
                }
            }
            System.out.println();
        }

    }
}
