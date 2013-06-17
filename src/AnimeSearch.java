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

/*
anime.php?type=0&score=0&status=0&tag=&p=0&r=0&sm=0&sd=0&sy=0&em=0&ed=0&ey=0&c[0]=a&c[1]=b&c[2]=c&gx=0&o=6&w=1&o=3&w=2&q=clannad
anime.php?c[]=a&c[]=b&c[]=c&c[]=d&c[]=e&c[]=f&c[]=g&q=clannad
anime.php?type=0&score=0&status=0&tag=&p=0&r=0&sm=0&sd=0&sy=0&em=0&ed=0&ey=0&c[0]=a&c[1]=b&c[2]=c&c[]=d&c[]=e&c[]=f&c[]=g&gx=0&o=6&w=1&o=3&w=2&q=clannad

sort by tags
score descending o=3 w=1
score ascending o=3 w=2
start date descending o=2 w=1
start date ascending o=2 w=2
end date ascending o=5 w=2
end date descending o=5 w=1
type special movie tv o=6 w=1
type tv movie special o=6 w=2
types Music ONA Special Movie OVA

*/
public class AnimeSearch {
    public AnimeSearchResult[] searchByQuery(String query) {
        String searchQueryString = "anime.php?c[]=a&c[]=b&c[]=c&c[]=d&c[]=e&c[]=f&c[]=g&q=" + query;
        return new AnimeSearchResults(new Network().connect(searchQueryString)).getSearchResults();
    }

    public AnimeSearchResult[] searchByQuery(String query, int page) {
        Integer pageQuery = 0;
        if (page >= 1) {
            pageQuery = (page - 1) * 20;
        }
        String searchQueryString = "anime.php?c[]=a&c[]=b&c[]=c&c[]=d&c[]=e&c[]=f&c[]=g&show=" + pageQuery.toString() + "&q=" + query;
        return new AnimeSearchResults(new Network().connect(searchQueryString)).getSearchResults();
    }

    public AnimeResult searchById(int id) {
        String searchUrl = "anime/" + Integer.toString(id);
        return new AnimeResult(new Network().connect_test1(searchUrl));
        //scrapeAnime(new Network().connect_test1(searchUrl));
    }
}
