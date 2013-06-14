/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 14/06/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Network {

    String rootUrl = "http://www.myanimelist.net/";

    public static HttpRequestFactory createRequestFactory(HttpTransport transport) {
        return transport.createRequestFactory();
    }

    public String connect(String query) {
        GenericUrl uri = new GenericUrl(rootUrl + "/" + query);
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        String result = null;
        try {
            HttpRequest req = requestFactory.buildGetRequest(uri);
            HttpResponse res = req.execute();
            InputStream in = res.getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String str = "";
            while ((str = buf.readLine()) != null) {
              sb.append(str);
            }
            result = sb.toString();
        } catch (Exception e) {

        }
        return result;
    }
}
