package youtubedownloader.classes;

import java.util.Objects;

public class YoutubeVideoFormat {
    public final String extension, desc;
    public final int id, sort, audioBitrate, resolution;
    public final boolean is3D;
    private static int counter = 1;

    public YoutubeVideoFormat(int id, int audioBitrate, int resolution, String extension, String desc, boolean is3D){
            this.id = id;
            this.extension = extension;
            this.resolution = resolution;
            this.audioBitrate = audioBitrate;
            this.desc = desc;
            this.is3D = is3D;
            this.sort = counter++;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof YoutubeVideoFormat){
            YoutubeVideoFormat yvf = (YoutubeVideoFormat)obj;
            return (id == yvf.id && extension.equals(yvf.extension) &&
                    audioBitrate == yvf.audioBitrate && is3D == yvf.is3D);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.extension);
        hash = 89 * hash + this.id;
        hash = 89 * hash + this.audioBitrate;
        hash = 89 * hash + (this.is3D ? 1 : 0);
        return hash;
    }

}
