package portfolio.saurabh.popularmovies;

/**
 * Created by Saurabh on 01-Mar-16.
 */
public class Trailer {
    public String id;
    public String key;
    public String site;
    public String type;

    public Trailer() {
    }

    public Trailer(String id, String key, String site, String type) {
        this.id = id;
        this.key = key;
        this.site = site;
        this.type = type;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
