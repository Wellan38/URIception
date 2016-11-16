public class URIObject {

    private String title;
    private String uri;
    private String relation;

    final String DEFAULT_TITLE = "Un objet";
    final String DEFAULT_RELATION = "Relation";

    public URIObject() {
        this.title = DEFAULT_TITLE;
        this.relation = DEFAULT_RELATION;
    }

    public String getTitle() {
        return title;
    }

    public URIObject setTitle(String title) {
        this.title = title;

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
