package hexa4304.uriception;

public class URIObject {

    private String title;
    private String content;

    final String DEFAULT_TITLE = "Un objet de beaucoup de caractères !";
    final String DEFAULT_CONTENT = "Un texte très long qui casse les couilles à prendre plusieurs lignes comme un fdp de merde. Bite.";

    public URIObject() {
        this.title = DEFAULT_TITLE;
        this.content = DEFAULT_CONTENT;
    }

    public URIObject(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public URIObject setTitle(String title) {
        this.title = title;

        return this;
    }

    public String getContent() {
        return content;
    }

    public URIObject setContent(String content) {
        this.content = content;

        return this;
    }
}
