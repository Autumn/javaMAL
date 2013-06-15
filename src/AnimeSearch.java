import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 14/06/13
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class AnimeSearch {

    private ArrayList<AnimeSearchResult> scrapeSearchResults(String site) {
        ArrayList<AnimeSearchResult> animeList = new ArrayList<AnimeSearchResult>();
        try {
            Document doc = Jsoup.parse(site);
            Elements resultsTable = doc.select("div#content div:eq(1) table");
            Elements resultsRows = resultsTable.select("tr");
            for (Element e: resultsRows) {
                Elements animeTitleNode = e.select("td a strong");
                if (animeTitleNode.size() == 0) {
                    continue;
                }

                AnimeSearchResult anime = new AnimeSearchResult();
                anime.setTitle(animeTitleNode.text());

                Element urlNode = animeTitleNode.parents().first();
                String url = urlNode.attr("href");
                String pattern = "http://myanimelist.net/anime/(\\d+)/?.*";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(url);
                if (m.find()) {
                    anime.setId(Integer.valueOf(m.group(1)));
                }

                Element imageNode = e.select("td a img").first();
                anime.setThumbUrl(imageNode.attr("src"));
                //anime.setImageUrl(modified thumb url);

                Elements tableCellNodes = e.select("td");
                anime.setEpisodes(Integer.valueOf(tableCellNodes.get(3).text()));
                anime.setMembersScore(Float.valueOf(tableCellNodes.get(4).text()));
                anime.setType(tableCellNodes.get(2).text());

                String startDate = tableCellNodes.get(5).text();
                String endDate = tableCellNodes.get(6).text();
                String classification = "";
                if (tableCellNodes.size() > 8) {
                    classification = tableCellNodes.get(8).text();
                }
                animeList.add(anime);
            }
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        return animeList;
    }

    private Anime scrapeAnime(String site) {
        System.out.println(site);
        return null;
    }

    public ArrayList<AnimeSearchResult> searchByQuery(String query) {
        String searchQueryString = "anime.php?c[]=a&c[]=b&c[]=c&c[]=d&c[]=e&c[]=f&c[]=g&q=" + query;
        return scrapeSearchResults(new Network().connect(searchQueryString));
    }

    public void searchById(int id) {
        String searchUrl = "anime/" + Integer.toString(id);
        scrapeAnime(new Network().connect(searchUrl));
    }
}
