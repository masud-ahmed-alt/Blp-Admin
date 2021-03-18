package www.blptimes.blpadmin.news;

public class NewsData {

    private String headLine,subHeadline, news, image, date, time, key;


    public NewsData() {
    }

    public NewsData(String headLine, String subHeadline, String news, String image, String date, String time, String key) {
        this.headLine = headLine;
        this.subHeadline = subHeadline;
        this.news = news;
        this.image = image;
        this.date = date;
        this.time = time;
        this.key = key;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getSubHeadline() {
        return subHeadline;
    }

    public void setSubHeadline(String subHeadline) {
        this.subHeadline = subHeadline;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
