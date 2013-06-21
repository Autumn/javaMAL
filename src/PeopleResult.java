import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 21/06/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class PeopleResult extends People {
    public PeopleResult(String siteText) {
        scrapePerson(siteText);
    }

    private void scrapePerson(String siteText) {
        Document doc = Jsoup.parse(siteText);
        Element detailsLink = doc.select("a:containsOwn(Details)").get(0);
        System.out.println(Utility.idFromUrl(detailsLink.attr("href")));
        Elements allContent = doc.select("div#contentWrapper");
        System.out.println(doc.select("h1").get(0).text()); // name
        Element content = allContent.select("div#content").get(0);
        System.out.println(content.select("img").attr("src")); // image url

        Elements givenNameNode = content.select("span:containsOwn(Given name:)");
        if (givenNameNode.size() > 0) {
            System.out.println(givenNameNode.get(0).nextSibling().toString().trim());
        }

        Elements familyNameNode = content.select("span:containsOwn(Family name:)");
        if (familyNameNode.size() > 0) {
            System.out.println(familyNameNode.get(0).nextSibling().toString().trim());
        }

        Elements birthdayNode = content.select("span:containsOwn(Birthday:)");
        if (birthdayNode.size() > 0) {
            System.out.println(birthdayNode.get(0).nextSibling().toString().trim());
        }

        Elements websiteNode = content.select("span:containsOwn(Website:)");
        if (websiteNode.size() > 0) {
            System.out.println(websiteNode.get(0).nextSibling().nextSibling().attr("href"));
        }

        Node moreNode = content.select("span:containsOwn(More:)").parents().get(0).nextSibling();
        StringBuilder moreText = new StringBuilder();
        while (moreNode != null) {
            if (moreNode.nodeName().equals("#text")) {
                moreText.append(moreNode.toString());
            } else {
                moreText.append("\n");
            }
            moreNode = moreNode.nextSibling();
        }
        System.out.println(moreText.toString());

        Elements seiyuuListNodes = content.select("div:containsOwn(Voice Acting Roles)");
        ArrayList<VoiceActingRole> seiyuuRoles = new ArrayList<VoiceActingRole>();
        if (!seiyuuListNodes.get(0).nextSibling().toString().matches(".*No voice acting roles have been added yet.*")) {
            Element table = seiyuuListNodes.get(0).nextElementSibling();
            for (Element rows : table.select("tr")) {
                Elements nodes = rows.select("td");
                Element imageNode = nodes.get(0);
                Element infoNode = nodes.get(1);
                Element charInfoNode = nodes.get(2);
                Element charImageNode = nodes.get(3);

                Integer animeId = Utility.idFromUrl(infoNode.select("a").get(0).attr("href"));
                String animeTitle = infoNode.select("a").text();
                String animeThumbUrl = imageNode.select("img").attr("src");
                String animeImageUrl = Utility.imageUrlFromThumbUrl(animeThumbUrl, 'v');

                Integer characterId = Utility.idFromUrl(charInfoNode.select("a").get(0).attr("href"));
                String characterName = charInfoNode.select("a").text();
                String characterRole = charInfoNode.select("div").text();
                String characterThumbUrl = charImageNode.select("img").attr("src");
                String characterImageUrl = Utility.imageUrlFromThumbUrl(characterThumbUrl, 't');
                seiyuuRoles.add(new VoiceActingRole(animeId, animeTitle, animeThumbUrl, animeImageUrl,
                        characterId, characterName, characterRole, characterThumbUrl, characterImageUrl));
            }
        }

        Elements animeStaffRolesNodes = content.select("div:containsOwn(Anime Staff Positions)");
        ArrayList<AnimeStaffRole> animeStaffRoleList = new ArrayList<AnimeStaffRole>();
        if (!animeStaffRolesNodes.get(0).nextSibling().toString().matches(".*This person has not worked on any anime.*")) {
            Element table = animeStaffRolesNodes.get(0).nextElementSibling();
            for (Element rows : table.select("tr")) {
                Elements nodes = rows.select("td");
                Element imageNode = nodes.get(0);
                Element infoNode = nodes.get(1);

                Integer animeId = Utility.idFromUrl(infoNode.select("a").attr("href"));
                String animeTitle = infoNode.select("a").text();
                String animeRole = infoNode.select("small").text() + infoNode.select("small").get(0).nextSibling().toString();
                String animeThumbUrl = imageNode.select("img").attr("src");
                String animeImageUrl = Utility.imageUrlFromThumbUrl(animeThumbUrl, 'v');
                animeStaffRoleList.add(new AnimeStaffRole(animeId, animeTitle, animeRole, animeThumbUrl, animeImageUrl));
            }
        }

        Elements publishedMangaNodes = content.select("div:containsOwn(Published Manga)");
        ArrayList<MangaStaffRole> mangaStaffRoleList = new ArrayList<MangaStaffRole>();

        if (!animeStaffRolesNodes.get(0).nextSibling().toString().matches(".*This person has not published any manga.*")) {
            Element table = animeStaffRolesNodes.get(0).nextElementSibling();
            for (Element rows : table.select("tr")) {
                Elements nodes = rows.select("td");
                Element imageNode = nodes.get(0);
                Element infoNode = nodes.get(1);

                Integer mangaId = Utility.idFromUrl(infoNode.select("a").attr("href"));
                String mangaTitle = infoNode.select("a").text();
                String mangaRole = infoNode.select("small").text() + infoNode.select("small").get(0).nextSibling().toString();
                String mangaThumbUrl = imageNode.select("img").attr("src");
                String mangaImageUrl = Utility.imageUrlFromThumbUrl(mangaThumbUrl, 'v');
                mangaStaffRoleList.add(new MangaStaffRole(mangaId, mangaTitle, mangaRole, mangaThumbUrl, mangaImageUrl));
            }
        }

    }
}
