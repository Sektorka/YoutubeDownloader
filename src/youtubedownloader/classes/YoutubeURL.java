package youtubedownloader.classes;

import java.util.Objects;

public class YoutubeURL {
    public final String url, title, youtubeURL;
    public final YoutubeVideoFormat videoformat;
    protected boolean convertToMp3;
    protected boolean removeOriginal;

    public YoutubeURL(String url, String youtubeURL, String title, YoutubeVideoFormat videoformat) {
        this.url = url;
        this.youtubeURL = youtubeURL;
        this.title = title;
        this.videoformat = videoformat;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof YoutubeURL){
            YoutubeURL u = (YoutubeURL)obj;
            return (title.equals(u.title) && youtubeURL.equals(u.youtubeURL) &&
                    videoformat.equals(u.videoformat));
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.title);
        hash = 53 * hash + Objects.hashCode(this.youtubeURL);
        hash = 53 * hash + Objects.hashCode(this.videoformat);
        return hash;
    }
    
    public boolean isConvertToMp3() {
        return convertToMp3;
    }

    public void setConvertToMp3(boolean convertToMp3) {
        this.convertToMp3 = convertToMp3;
    }

    public boolean isRemoveOriginal() {
        return removeOriginal;
    }

    public void setRemoveOriginal(boolean removeOriginal) {
        this.removeOriginal = removeOriginal;
    }
}
