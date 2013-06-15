/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 14/06/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.UriTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class Network {

    String rootUrl = "http://www.myanimelist.net/";

    public String connect(String query) {
        String siteUrl = rootUrl + query;
        String result = "";
        try {
            URL url = new URL(siteUrl);
            URLConnection urlc = url.openConnection();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = buffer.readLine()) != null) {
                sb.append(str);
            }
            result = sb.toString();

        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        return result;
    }
}
