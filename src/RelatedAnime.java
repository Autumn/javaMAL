/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 16/06/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class RelatedAnime {
    public Integer id;
    public String title;
    public String type;

    public RelatedAnime(Integer id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public String toString() {
        return id.toString() + " " + title + " " + type;
    }
}
