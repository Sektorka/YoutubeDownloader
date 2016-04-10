package youtubedownloader.classes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import youtubedownloader.forms.MainForm;

public class Download implements Runnable{
    private YoutubeURLOpt url;
    private long lastUpdate, lastUpdateRemaing;
    private long currRemaing, prevDownloaded, downloadSpeed;
    private int sc, prevPercent;
    
    //public static final String APP_FFMPEG = "ffmpeg.exe";
    
    private static final String CONVERTED_PATTERN = "^size=(.*) time=(.*) bitrate=(.*)";
    
    private InputStream input;
    private OutputStream output;
    
    public Download(YoutubeURLOpt url){
        this.url = url;
        
        lastUpdate = lastUpdateRemaing = (new Date()).getTime();
        currRemaing = prevDownloaded = downloadSpeed = 0;
        sc = 2;
    }
    
    public void closeStreams(){
        try{
            input.close();
            output.close();
        }
        catch(Exception e){}
    }
    
    @Override
    public void run() {
        try{
            URL url = new URL(this.url.url);
            URLConnection connection = new URL(this.url.url).openConnection();
            
            connection.setDoInput(true);
            connection.setDoOutput(true);

            File file = new File(this.url.getFullPath());
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            
            boolean append = false;
            if(file.exists()){
                append = true;
                connection.setRequestProperty("Range", "bytes="+file.length()+"-");
                total = file.length();
            }
            
            input = new BufferedInputStream(url.openStream());
            output = new FileOutputStream(file.getAbsolutePath(),append);
            
            MainForm.getInstance().setStatus(this.url, MainForm.STATE_DOWNLOADING);
            
            while (total < this.url.getFileSize()) {
                try{
                    count = input.read(data);
                    if(count == -1) continue;
                    total += count;
                    totalDownloaded(total);
                    output.write(data, 0, count);
                    output.flush();
                }
                catch(Exception e){
                    Thread.sleep(1000);
                    try{
                        connection = url.openConnection();
                        connection.setRequestProperty("Range", "bytes="+total+"-");
                        input = new BufferedInputStream(url.openStream());
                    }
                    catch(Exception ex){}
                }
            }
            
            input.close();
            output.close();
            
            if(this.url.isConvertToMp3()){
                double vlength = getVideoLength();
                
                String command = (Settings.isWindows() ? "cmd /c " : "") + 
                        Settings.getInstance().getFfmpegPath() + 
                        " -i " + this.url.getFullPath() + " " + 
                        this.url.getFullMp3Path() + " -y";
                
                String line;
                
                MainForm.getInstance().setStatus(this.url, MainForm.STATE_CONVERTING);
                
                Process p = Runtime.getRuntime().exec(command);
                
                BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
                
                while ((line = bre.readLine()) != null){
                    Matcher matcher = Pattern.compile(CONVERTED_PATTERN).matcher(line);
                    if(matcher.matches()){
                        int percent = (int)((Functions.getSeconds(matcher.group(2)) / vlength) * 100);
                        MainForm.getInstance().setProgess(this.url, percent);
                    }
                }
                
                bre.close();
                p.waitFor();
                
                if(this.url.isRemoveOriginal()){
                    file.delete();
                }
            }
            
            MainForm.getInstance().setProgess(this.url, 100);
            MainForm.getInstance().setStatus(this.url, MainForm.STATE_COMPLETED);
        }
        catch(IOException e){
            MainForm.getInstance().setStatus(this.url, MainForm.STATE_ERROR);
            JOptionPane.showMessageDialog(MainForm.getInstance(), "Adatáviteli hiba történt letöltés közben!\n" + e.getMessage(), "Letöltés vezérlő", JOptionPane.ERROR_MESSAGE);
            
        }
        catch(InterruptedException e){
            MainForm.getInstance().setStatus(this.url, MainForm.STATE_STOPPED);
            //JOptionPane.showMessageDialog(MainForm.getInstance(), "Adatáviteli hiba történt letöltés közben!\n" + e.getMessage(), "Letöltés vezérlő", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private double getVideoLength() throws IOException, InterruptedException{
        return MainForm.getFFMpegDuration(this.url.getFullPath());
    }
    
    private void totalDownloaded(long downloaded){
        
        int percent = (url.getFileSize() == 0 ? 0 : (int)(downloaded * 100 / url.getFileSize()));

        if(percent >= 100){
            percent = 100;
            currRemaing = 0;
            downloadSpeed = 0;
            MainForm.getInstance().setRemaingTime(url, currRemaing, true);
            MainForm.getInstance().setSpeed(url, downloadSpeed);
            MainForm.getInstance().setProgess(url, percent);
            this.url.setDownloadState(YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED);
            MainForm.getInstance().updateToolbar();
            return;
        }
        
        if(prevPercent != percent){
            MainForm.getInstance().setProgess(url, percent);
            prevPercent = percent;
        }
        
        if(downloaded > 0 && ( (new Date()).getTime()  - lastUpdate >= 1000 
                || downloaded == url.getFileSize())){
            sc++;
            downloadSpeed = downloaded - prevDownloaded;
            
            if(downloadSpeed < 0){
                downloadSpeed = 0;
            }
            
            prevDownloaded = downloaded;
            
            lastUpdate = (new Date()).getTime();
            
            if(currRemaing > 10){
            	currRemaing--;
            }
            
            MainForm.getInstance().setSpeed(url, downloadSpeed);
            
            if(sc > 5){
                MainForm.getInstance().setRemaingTime(url, currRemaing);
            }
        }
        
        if(sc > 4){
            if(downloaded > 0 && ( (new Date()).getTime()  - lastUpdateRemaing >= 
                    (currRemaing < 11 ? 1000 : 5000) || downloaded == url.getFileSize())){
                currRemaing = (int)((((double)url.getFileSize()-downloaded) / downloadSpeed));
                lastUpdateRemaing = (new Date()).getTime();
            }
        }
    }
}
