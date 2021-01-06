/**
 * @author kjell
 */

package ProjectMovieCollection.be;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Movie {

    private int id;
    private String title;
    private String desc;
    private int rating;
    private String filepath;
    private String imgPath;
    private Date lastView;

    private List<Category> categories;

    public Movie(int id ,String title, String filepath) {
        this.id = id;
        this.title = title;
        this.filepath = filepath;

        lastView = Date.from(Instant.now());
        rating = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Date getLastView() {
        return lastView;
    }

    public void setLastView(Date lastView) {
        this.lastView = lastView;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return title;
    }
}
