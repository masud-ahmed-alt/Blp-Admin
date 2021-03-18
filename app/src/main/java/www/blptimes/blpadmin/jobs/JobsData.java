package www.blptimes.blpadmin.jobs;

public class JobsData {

    private String jobTitle,noPosts, jobDescriptions, howApply,advLinks,applyLinks,image,date, time, key;

    public JobsData() {
    }

    public JobsData(String jobTitle, String noPosts, String jobDescriptions, String howApply, String advLinks, String applyLinks, String image, String date, String time, String key) {
        this.jobTitle = jobTitle;
        this.noPosts = noPosts;
        this.jobDescriptions = jobDescriptions;
        this.howApply = howApply;
        this.advLinks = advLinks;
        this.applyLinks = applyLinks;
        this.image = image;
        this.date = date;
        this.time = time;
        this.key = key;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getNoPosts() {
        return noPosts;
    }

    public void setNoPosts(String noPosts) {
        this.noPosts = noPosts;
    }

    public String getJobDescriptions() {
        return jobDescriptions;
    }

    public void setJobDescriptions(String jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
    }

    public String getHowApply() {
        return howApply;
    }

    public void setHowApply(String howApply) {
        this.howApply = howApply;
    }

    public String getAdvLinks() {
        return advLinks;
    }

    public void setAdvLinks(String advLinks) {
        this.advLinks = advLinks;
    }

    public String getApplyLinks() {
        return applyLinks;
    }

    public void setApplyLinks(String applyLinks) {
        this.applyLinks = applyLinks;
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
