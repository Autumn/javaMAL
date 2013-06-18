import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 18/06/13
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CharacterResult extends Characters {
    public CharacterResult(String siteText) {
        scrapeResult(siteText);
    }

    private void scrapeResult(String siteText) {
        try {
            Document doc = Jsoup.parse(siteText);
            Element detailsLink = doc.select("a:containsOwn(Details)").get(0);
            System.out.println(detailsLink.attr("href")); // id
            System.out.println(doc.select("h1").text()); // name
            System.out.println(doc.select("div#content tr td div img").get(0).attr("src")); // image

            Elements leftColumnNodeset = doc.select("div#content table tr td.borderClass");
            Element animeTable = leftColumnNodeset.select("div:containsOwn(Animeography").get(0).nextElementSibling();
            Element mangaTable = leftColumnNodeset.select("div:containsOwn(Mangaography").get(0).nextElementSibling();

            for (Element animeNode : animeTable.select("tr")) {
                Elements infoNodes = animeNode.select("td");
                Element imageNode = infoNodes.get(0);
                Element titleNode = infoNodes.get(1);
                //System.out.println(imageNode);
                //System.out.println(titleNode);
            }

            for (Element mangaNode : mangaTable.select("tr")) {
                Elements infoNodes = mangaNode.select("td");
                Element imageNode = infoNodes.get(0);
                Element titleNode = infoNodes.get(1);
                //System.out.println(imageNode);
                //System.out.println(titleNode);
            }

            Elements contentDivs = doc.select("div.normal_header");
            Element nameDiv = contentDivs.get(2);
            Element seiyuuDiv = contentDivs.get(3);

            System.out.println(nameDiv.childNode(0).toString().trim()); // eng name
            System.out.println(nameDiv.childNode(1)); // jp name

            StringBuilder bio = new StringBuilder();
            Node bioNode = nameDiv.nextSibling();
            while (!bioNode.equals(seiyuuDiv)) {
                bio.append(bioNode.toString());
                bioNode = bioNode.nextSibling();
            }

            seiyuuDiv = seiyuuDiv.nextElementSibling();
            System.out.println(seiyuuDiv);
            while (!seiyuuDiv.nodeName().equals("br")) {
                //System.out.println(seiyuuDiv);
                seiyuuDiv = seiyuuDiv.nextElementSibling();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
