import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Arrays;
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

    class OtherTitles {
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

    class Genres {
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

    class Producers {
        Integer id;
        String name;
        public Producers(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
        public String toString() {
            return id + " " + name;
        }
    }

    class RelatedAnime {
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


    class RelatedManga {
        public Integer id;
        public String title;
        public String type;

        public RelatedManga(Integer id, String title, String type) {
            this.id = id;
            this.title = title;
            this.type = type;
        }

        public String toString() {
            return id.toString() + " " + title + " " + type;
        }
    }

    class AnimeSearchCharacter {
        Integer id;
        String name;
        String role;
        String thumbUrl;
        String imageUrl;
        AnimeSearchSeiyuu[] seiyuus;
        public AnimeSearchCharacter(Integer id, String name, String role, String thumbUrl, String imageUrl, AnimeSearchSeiyuu[] seiyuus) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.thumbUrl = thumbUrl;
            this.imageUrl = imageUrl;
            this.seiyuus = seiyuus;
        }
    }

    class AnimeSearchSeiyuu {
        Integer id;
        String name;
        String nation;
        String thumbUrl;
        String imageUrl;
        public AnimeSearchSeiyuu(Integer id, String name, String nation, String thumbUrl, String imageUrl) {
            this.id = id;
            this.name = name;
            this.nation = nation;
            this.thumbUrl = thumbUrl;
            this.imageUrl = imageUrl;
        }
    }

    class AnimeSearchStaff {
        Integer id;
        String name;
        String role;
        String thumbUrl;
        String imageUrl;
        public AnimeSearchStaff(Integer id, String name, String role, String thumbUrl, String imageUrl) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.thumbUrl = thumbUrl;
            this.imageUrl = imageUrl;
        }
    }

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

            // type - Adaptation/Prequel/Sequel/Character/Spin-off/Summary/Alternative versions
            // link and name
            // ", " if more, <br /> if finished
            // h2 when completely done

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

            String charactersLink = doc.select("a:containsOwn(More characters)").get(0).attr("href");
            String charactersPage = new Network().connect_url(charactersLink);
            scrapeCharactersStaffPage(charactersPage);
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        return null;
    }


    class CharactersStaff {
        AnimeSearchCharacter[] characters;
        AnimeSearchStaff[] staff;
        CharactersStaff(AnimeSearchCharacter[] characters, AnimeSearchStaff[] staff) {
            this.characters = characters;
            this.staff = staff;
        }
    }

    private CharactersStaff scrapeCharactersStaffPage(String body) {
        Document doc = Jsoup.parse(body);
        Elements nodes = doc.select("h2:containsOwn(Characters & Voice Actors) + table").parents().get(0).select("table");
        Element parentDiv = doc.select("h2:containsOwn(Characters & Voice Actors)").parents().get(0);
        Element staffNode = nodes.get(nodes.size() - 1);
        ArrayList<AnimeSearchCharacter> characterList = new ArrayList<AnimeSearchCharacter>();
        ArrayList<AnimeSearchStaff> staffList = new ArrayList<AnimeSearchStaff>();
        for (Element node : nodes) {
            if (!node.equals(staffNode)) {
                if (!node.parent().equals(parentDiv)) continue; // required so the nested tables are skipped
                
                Elements characterNodes = node.select("tr").get(0).select("td");
                Element characterInfoNode = characterNodes.get(1);
                Element characterImageNode = characterNodes.get(0);

                String charUrl = characterInfoNode.select("a").get(0).attr("href");
                Integer charId = idFromUrl(charUrl);
                String charRole = characterInfoNode.select("small").text();
                String charName = characterInfoNode.select("a").get(0).ownText();
                String charThumbUrl = characterImageNode.select("a img").attr("src");
                String charImageUrl = imageUrlFromThumbUrl(charThumbUrl, 't');

                ArrayList<AnimeSearchSeiyuu> seiyuuList = new ArrayList<AnimeSearchSeiyuu>();
                for (Element seiyuuNode : node.select("table tr")) {
                    Elements infoNodes = seiyuuNode.select("td");
                    if (infoNodes.size() > 0) {
                        Element seiyuuInfoNode = infoNodes.get(0);
                        Element seiyuuImageNode = infoNodes.get(1);
                        String seiyuuUrl = seiyuuInfoNode.select("a").attr("href");
                        Integer seiyuuId = idFromUrl(seiyuuUrl);
                        String seiyuuNation = seiyuuInfoNode.select("small").text();
                        String seiyuuName = seiyuuInfoNode.select("a").text();
                        String seiyuuThumbUrl = seiyuuImageNode.select("a img").attr("src");
                        String seiyuuImageUrl = imageUrlFromThumbUrl(seiyuuThumbUrl, 'v');
                        seiyuuList.add(new AnimeSearchSeiyuu(seiyuuId, seiyuuName, seiyuuNation, seiyuuThumbUrl, seiyuuImageUrl));
                    }
                }
                Object[] array = seiyuuList.toArray();
                AnimeSearchSeiyuu[] seiyuuArray = Arrays.copyOf(array, array.length, AnimeSearchSeiyuu[].class);
                characterList.add(new AnimeSearchCharacter(charId, charName, charRole, charThumbUrl, charImageUrl, seiyuuArray));
            } else {
                for (Element row : node.select("tr")) {
                    Elements staffNodes = row.select("td");
                    String staffUrl = staffNodes.get(0).select("a").attr("href");
                    Integer staffId = idFromUrl(staffUrl);
                    String staffThumbUrl = staffNodes.get(0).select("img").attr("src");
                    String staffImageUrl = imageUrlFromThumbUrl(staffThumbUrl, 'v');
                    String staffName = staffNodes.get(1).select("a").text();
                    String staffRole = staffNodes.get(1).select("small").text();
                    staffList.add(new AnimeSearchStaff(staffId, staffName, staffRole, staffThumbUrl, staffImageUrl));
                }
            }
        }
        Object[] array = characterList.toArray();
        AnimeSearchCharacter[] characterArray = Arrays.copyOf(array, array.length, AnimeSearchCharacter[].class);
        array = staffList.toArray();
        AnimeSearchStaff[] staffArray = Arrays.copyOf(array, array.length, AnimeSearchStaff[].class);

        return new CharactersStaff(characterArray, staffArray);
    }

    private Integer idFromUrl(String url) {
        String[] str = url.split("/");
        return Integer.valueOf(str[str.length - 2]);
    }

    private String imageUrlFromThumbUrl(String thumbUrl, char thumbChar) {
        int char_pos = thumbUrl.lastIndexOf(".");
        if (thumbUrl.charAt(char_pos - 1) == thumbChar) {
            return thumbUrl.substring(0, char_pos - 1) + thumbUrl.substring(char_pos, thumbUrl.length());
        }
        return thumbUrl;
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
        scrapeAnime(new Network().connect_test1(searchUrl));
    }
}
