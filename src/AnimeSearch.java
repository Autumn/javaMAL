import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Arrays;
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
    public AnimeSearchResult[] searchByQuery(String query) {
        String searchQueryString = "anime.php?c[]=a&c[]=b&c[]=c&c[]=d&c[]=e&c[]=f&c[]=g&q=" + query;
        return new AnimeSearchResults(new Network().connect(searchQueryString)).getSearchResults();
    }

    public AnimeResult searchById(int id) {
        String searchUrl = "anime/" + Integer.toString(id);
        return new AnimeResult(new Network().connect_test1(searchUrl));
        //scrapeAnime(new Network().connect_test1(searchUrl));
    }
}
