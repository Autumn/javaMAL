import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 14/06/13
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Harness {
    public static void main(String[] args) {
        //System.out.println(new AnimeSearch().searchById(2167));
        //System.out.println(Arrays.toString(new AnimeSearch().searchByQuery("1", 2)));
        new CharacterSearch().searchById(4605);
    }
}
