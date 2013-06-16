/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 16/06/13
 * Time: 8:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class OtherTitles {
    public String language;
    public String title;
    public OtherTitles(String language, String title) {
        this.language = language;
        this.title = title;
    }

    public String toString() {
        return language + " " + title;
    }
}
