/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 16/06/13
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class Genres {
    Integer id;
    String name;
    public Genres(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public String toString() {
        return id + " " + name;
    }

}
