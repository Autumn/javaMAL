/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 17/06/13
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class Utility {
    public static Integer idFromUrl(String url) {
        String[] str = url.split("/");
        return Integer.valueOf(str[str.length - 2]);
    }

    public static String imageUrlFromThumbUrl(String thumbUrl, char thumbChar) {
        int char_pos = thumbUrl.lastIndexOf(".");
        if (thumbUrl.charAt(char_pos - 1) == thumbChar) {
            return thumbUrl.substring(0, char_pos - 1) + thumbUrl.substring(char_pos, thumbUrl.length());
        }
        return thumbUrl;
    }
}
