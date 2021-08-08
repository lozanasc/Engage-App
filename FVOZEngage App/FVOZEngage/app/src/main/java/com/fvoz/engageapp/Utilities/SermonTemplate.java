package com.fvoz.engageapp.Utilities;

public class SermonTemplate {

    private String SermonTitle;
    private String SermonShortURL;
    private String Duration;
    private String ThumbnailURL;

    public SermonTemplate(){}
    public SermonTemplate( String Title, String ShortURL, String Duration, String ThumbnailURL){
        this.SermonShortURL = ShortURL;
        this.SermonTitle = Title;
        this.ThumbnailURL = ThumbnailURL;
        this.Duration = Duration;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getSermonTitle() {
        return SermonTitle;
    }

    public void setSermonTitle(String sermonTitle) {
        SermonTitle = sermonTitle;
    }

    public String getSermonShortURL() {
        return SermonShortURL;
    }

    public void setSermonShortURL(String sermonShortURL) {
        SermonShortURL = sermonShortURL;
    }

    public String getThumbnailURL() {
        return ThumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        ThumbnailURL = thumbnailURL;
    }
}
