import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: aki
 * Date: 14/06/13
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class AnimeSearch {

    private ArrayList<AnimeSearchResult> scrapeSearchResults(String site) {
        ArrayList<AnimeSearchResult> animeList = new ArrayList<AnimeSearchResult>();
        try {
            Document doc = Jsoup.parse(site);
            Elements resultsTable = doc.select("div#content div:eq(1) table");
            Elements resultsRows = resultsTable.select("tr");
            for (Element e: resultsRows) {
                Elements animeTitleNode = e.select("td a strong");
                if (animeTitleNode.size() == 0) {
                    continue;
                }

                AnimeSearchResult anime = new AnimeSearchResult();
                anime.setTitle(animeTitleNode.text());

                Element urlNode = animeTitleNode.parents().first();
                String url = urlNode.attr("href");
                String pattern = "http://myanimelist.net/anime/(\\d+)/?.*";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(url);
                if (m.find()) {
                    anime.setId(Integer.valueOf(m.group(1)));
                }

                Element imageNode = e.select("td a img").first();
                anime.setThumbUrl(imageNode.attr("src"));
                //anime.setImageUrl(modified thumb url);

                Elements tableCellNodes = e.select("td");
                anime.setEpisodes(Integer.valueOf(tableCellNodes.get(3).text()));
                anime.setMembersScore(Float.valueOf(tableCellNodes.get(4).text()));
                anime.setType(tableCellNodes.get(2).text());

                String startDate = tableCellNodes.get(5).text();
                String endDate = tableCellNodes.get(6).text();
                String classification = "";
                if (tableCellNodes.size() > 8) {
                    classification = tableCellNodes.get(8).text();
                }
                animeList.add(anime);
            }
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        return animeList;
    }

    private Anime scrapeAnime(String site) {
        AnimeResult anime = new AnimeResult();
        try {
            Document doc = Jsoup.parse(site);
            Element animeIdInput = doc.select("input[name=aid]").get(0);
            anime.setId(Integer.valueOf(animeIdInput.attr("value")));
            anime.setTitle(doc.select("h1").get(0).ownText());
            System.out.println(doc.select("h1 > div").text()); // Ranked #XX
            System.out.println(doc.select("div#content tr td div img").attr("src")); // thumb url
            Elements leftColumnNodeset = doc.select("div#content table tr td.borderClass");
            //System.out.println(leftColumnNodeset);

            Elements otherEnglishTitles = doc.select("span:containsOwn(English:)");
            Elements otherJapaneseTitles = doc.select("span:containsOwn(Japanese:)");
            Elements otherSynonymTitles = doc.select("span:containsOwn(Synonyms:)");
            ArrayList<OtherTitles> otherTitles = new ArrayList<OtherTitles>();
            otherTitles.add(new OtherTitles("english", textIfExists(otherEnglishTitles)));
            otherTitles.add(new OtherTitles("japanese", textIfExists(otherJapaneseTitles)));
            otherTitles.add(new OtherTitles("synonyms", textIfExists(otherSynonymTitles)));

            Elements typeNodes = doc.select("span:containsOwn(Type:)");
            System.out.println(textIfExists(typeNodes));

            Elements episodesNodes = doc.select("span:containsOwn(Episodes:");
            System.out.println(textIfExists(episodesNodes));

            Elements statusNodes = doc.select("span:containsOwn(Status:)");
            System.out.println(textIfExists(statusNodes));

            Elements airedNodes = doc.select("span:containsOwn(Aired:)");
            System.out.println(textIfExists(airedNodes));

            Elements producersLinks = doc.select("span:containsOwn(Producers:").parents().get(0).select("a");
            ArrayList<Producers> producers = new ArrayList<Producers>();
            for (Element link : producersLinks) {
                String url = link.attr("href");
                Integer id = Integer.valueOf(url.split("=")[1]);
                String name = link.text();
                producers.add(new Producers(id, name));
            }
            System.out.println(producers);

            Elements genresLinks = doc.select("span:containsOwn(Genres:)").parents().get(0).select("a");
            ArrayList<Genres> genres = new ArrayList<Genres>();
            for (Element link : genresLinks) {
                String url = link.attr("href");
                Integer id = Integer.valueOf(url.split("=")[1]);
                String name = link.text();
                genres.add(new Genres(id, name));
            }

            System.out.println(genres);

            Elements classificationNodes = doc.select("span:containsOwn(Rating:)");
            System.out.println(textIfExists(classificationNodes));

            Elements scoreNodes = doc.select("span:containsOwn(Score:)");
            System.out.println(textIfExists(scoreNodes));

            Elements popularityNodes = doc.select("span:containsOwn(Popularity:)");
            System.out.println(textIfExists(popularityNodes));

            Elements membersNodes = doc.select("span:containsOwn(Members:)");
            System.out.println(textIfExists(membersNodes));

            Elements favouritesNodes = doc.select("span:containsOwn(Favorites:)");
            System.out.println(textIfExists(favouritesNodes));

            Elements popularTagsNodes = doc.select("h2:containsOwn(Popular Tags) + span").get(0).select("a");
            ArrayList<String> popularTags = new ArrayList<String>();
            for (Element link : popularTagsNodes) {
                popularTags.add(link.text());
            }
            System.out.println(popularTags);

            Element synopsisNode = doc.select("h2:containsOwn(Synopsis)").get(0);
            StringBuilder sb = new StringBuilder();
            Node synopsis = synopsisNode.nextSibling();

            do {
                sb.append(synopsis.toString());
            } while ((synopsis = synopsis.nextSibling()) != null);
            System.out.println(sb.toString());

            Elements relatedAnimeNodes = doc.select("h2:containsOwn(Related Anime)");
            Node relatedAnimeNode = relatedAnimeNodes.get(0).nextSibling();

            String type = "";
            ArrayList<RelatedAnime> relatedAnime = new ArrayList<RelatedAnime>();
            while (!relatedAnimeNode.nodeName().equals("h2")) {
                String text = relatedAnimeNode.toString();
                if (text.matches(".*Adaptation:.*")) {
                    type = "Adaptation";
                } else if (text.matches(".*Prequel:.*")) {
                    type = "Prequel";
                } else if (text.matches(".*Sequel:.*")) {
                    type = "Sequel";
                } else if (text.matches(".*Character.*")) {
                    type = "Character";
                } else if (text.matches(".*Spin-off.*")) {
                    type = "Spin-off";
                } else if (text.matches(".*Summary.*")) {
                    type = "Summary";
                } else if (text.matches(".*Alternative version:.*")) {
                    type = "Alternative version";
                } else if (text.matches(".*Alternative setting:.*")) {
                    type = "Alternative setting";
                } else if (!text.matches(".*<br.*") && !text.matches(".*,.*") && !text.matches(" ")) {
                    String title = relatedAnimeNode.childNode(0).toString();
                    Integer id = 0;
                    String pattern = "http://myanimelist.net/(\\w+)/(\\d+)/?.*";
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(relatedAnimeNode.attr("href").toString());
                    if (m.find()) {
                        id = Integer.valueOf(m.group(2));
                    }
                    relatedAnime.add(new RelatedAnime(id, title, type));
                }
                relatedAnimeNode = relatedAnimeNode.nextSibling();
            }

            System.out.println(relatedAnime);

            // type - Adaptation/Prequel/Sequel/Character/Spin-off/Summary/Alternative versions
            // link and name
            // ", " if more, <br /> if finished
            // h2 when completely done


        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }

        return null;
    }

    private String textIfExists(Elements nodes) {
        if (nodes.size() > 0) {
            return nodes.parents().get(0).ownText();
        }
        return null;
    }

    public ArrayList<AnimeSearchResult> searchByQuery(String query) {
        String searchQueryString = "anime.php?c[]=a&c[]=b&c[]=c&c[]=d&c[]=e&c[]=f&c[]=g&q=" + query;
        return scrapeSearchResults(new Network().connect(searchQueryString));
    }

    public void searchById(int id) {
        String searchUrl = "anime/" + Integer.toString(id);
        scrapeAnime(new Network().connect(searchUrl));
    }
}
