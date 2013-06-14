/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 14/06/13
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class AnimeSearch {
    private Anime scrapeSearchResults(String site) {
        System.out.println(site);
        return null;
    }

    private Anime scrapeAnime(String site) {
        System.out.println(site);
        return null;
    }

    public void searchByQuery(String query) {
        String searchUrl = "anime.php?q=" + query;
        scrapeSearchResults(new Network().connect(searchUrl));
    }

    public void searchById(int id) {
        String searchUrl = "anime/" + Integer.toString(id);
        scrapeAnime(new Network().connect(searchUrl));
    }
}
