package youtubedownloader.classes;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import youtubedownloader.forms.MainForm;


public class YoutubeURLOpt extends YoutubeURL {
    protected static final String REPLACE_PATTERN = "( |\\\\|:|\"|<|>|\\||\\?)";
    protected static String REPLACEMENT;
    
    public static final int DOWNLOAD_STATE_PENDING = 0;
    public static final int DOWNLOAD_STATE_DOWNLOADING = 1;
    public static final int DOWNLOAD_STATE_STOPPED = 2;
    public static final int DOWNLOAD_STATE_DOWNLOADED = 3;
    
    protected int downloadState = 0;
    protected String dirPath;
    protected Thread thread;
    protected Download download;
    protected long fileSize;
    protected double duration;
    
    public YoutubeURLOpt(YoutubeURL url, String dirPath){
        this(url, dirPath, 0, 0);
    }
    
    public YoutubeURLOpt(YoutubeURL url, String dirPath, long fileSize, double duration){
        super(url.url, url.youtubeURL, url.title, url.videoformat);
        convertToMp3 = url.convertToMp3;
        removeOriginal = url.removeOriginal;
        
        REPLACEMENT = Settings.getInstance().getReplaceKeyWord();
        this.dirPath = dirPath;
        this.duration = duration;
        
        if(fileSize == 0){
            try{
                URL u = new URL(url.url);
                URLConnection connection = new URL(url.url).openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);

                this.fileSize = connection.getContentLengthLong();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(MainForm.getInstance(), "Hiba történt a videó méretének lekérdezése közben!\n" + e.getMessage(),
                        "Youtube letöltő", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            this.fileSize = fileSize;
        }
    }
    
    public int getDownloadState(){
        return downloadState;
    }
    
    public String getFileName(){
        return title.replaceAll(REPLACE_PATTERN, REPLACEMENT) + REPLACEMENT + 
                videoformat.resolution + "p" + 
                (videoformat.is3D ? REPLACEMENT + "3D" : "") +
                "." + videoformat.extension;
    }
    
    public String getFullPath(){
        if(dirPath.charAt(dirPath.length()-1)!= File.separator.charAt(0)){
            dirPath += File.separator;
        }
        
        return dirPath + getFileName();
    }
    
    public String getFullMp3Path(){
        return getFullPath().substring(0,getFullPath().lastIndexOf(".")) + ".mp3";
    }
    
    public String getDir(){
        return dirPath;
    }
    
    public long getCurrentFileSize(){
        return new File(getFullPath()).length();
    }
    
    public long getFileSize(){
        return fileSize;
    }
        
    public void setFileSize(long fileSize){
        this.fileSize = fileSize;
    }
    
    public void startDownload(){
        if(downloadState == DOWNLOAD_STATE_DOWNLOADING || downloadState == DOWNLOAD_STATE_DOWNLOADED)
            return;
        
        if(download == null){
            download = new Download(this);
        }
        
        if(thread == null){
            thread = new Thread(download);
        }
        
        if(downloadState == DOWNLOAD_STATE_PENDING || downloadState == DOWNLOAD_STATE_STOPPED){
            thread.start();
        }
        
        downloadState = DOWNLOAD_STATE_DOWNLOADING;
    }
    
    public void stopDownload(){
        if(downloadState == DOWNLOAD_STATE_STOPPED || downloadState == DOWNLOAD_STATE_DOWNLOADED)
            return;
        
        if(thread != null){
            
            thread.stop();
            thread = null;
            
            download.closeStreams();
            
            MainForm.getInstance().setSpeed(this, 0);
            MainForm.getInstance().setRemaingTime(this, 0);
        }
        
        downloadState = DOWNLOAD_STATE_STOPPED;
    }
    
    public void setDownloadState(int downloadState){
        this.downloadState = downloadState;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}


