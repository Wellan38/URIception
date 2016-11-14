package hexa4304.uriception;

public class URIObject {

    private String title;
    private String uri;
    private String content;
    private String relation;

    final String DEFAULT_TITLE = "Un objet de beaucoup de caractères !";
    final String DEFAULT_CONTENT = "Un texte très long qui casse les couilles à prendre plusieurs lignes comme un fdp de merde. Bite.";
    final String DEFAULT_RELATION = "Relation";

    public URIObject() {
        this.title = DEFAULT_TITLE;
        this.content = DEFAULT_CONTENT;
        this.relation = DEFAULT_RELATION;
    }

    public URIObject(String title, String content, String relation) {
        this.title = title;
        this.content = content;
        this.relation = relation;
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

    public String getUri() {
        return uri;
    }

    public URIObject setUri(String uri) {
        this.uri = uri;

        return this;
    }

    public String getRelation() {
        return relation;
    }

    public URIObject setRelation(String relation) {
        this.relation = relation;

        return this;
    }
}
