import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 14/06/13
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Harness {
    public static void main(String[] args) {
        ArrayList<AnimeSearchResult> animeSearchResult = new AnimeSearch().searchByQuery("clannad");
        System.out.println(animeSearchResult);
    }
}
