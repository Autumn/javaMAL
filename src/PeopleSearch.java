/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 20/06/13
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class PeopleSearch {
    public PeopleSearch searchById(int id) {
        String searchUrl = "people/" + Integer.toString(id);
        //return new CharacterResult(new Network().connect(searchUrl));
        return null;
    }

    public PeopleSearchResults searchByQuery(String query) {
        String searchUrl = "people.php?q=" + query;
        return new PeopleSearchResults(new Network().connect(searchUrl));
    }
}
