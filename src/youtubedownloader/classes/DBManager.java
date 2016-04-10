package youtubedownloader.classes;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

public class DBManager {
    private static DBManager dbmanager;
    private static final String DRIVER = "org.sqlite.JDBC"; //"com.mysql.jdbc.Driver";
    
    Connection connection = null;
    
    public static DBManager instance(){
        if(dbmanager == null){
            dbmanager = new DBManager();
        }
        
        return dbmanager;
    }
    
    private DBManager(){
        try{
            checkTables();
        }
        catch(SQLException e){
            
        }
    }
    
    private void checkTables() throws SQLException{
        if(connect()){
        
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS videos (" +
                         "youtube_url       TEXT        NOT NULL, " +
                         "title             TEXT        NOT NULL, "+
                         "dirpath           TEXT        NOT NULL, "+
                         "filesize          INTEGER     DEFAULT 0, "+
                         "videoformat       INTEGER     DEFAULT 0, "+
                         "added             INTEGER     DEFAULT 0, "+
                         "download_state    INTEGER     DEFAULT 0, "+ 
                         "convert_to_mp3    INTEGER     DEFAULT 0, "+
                         "remove_original   INTEGER     DEFAULT 0, "+
                         "duration          REAL        DEFAULT 0, "+
                         "PRIMARY KEY (youtube_url, videoformat)"+
                         ")"; 
            stmt.executeUpdate(sql);
            stmt.close();

            close();
        }
    }
    
    private boolean connect(){
        try{
            Class.forName(DRIVER);
            
            //String url = "jdbc:mysql://" + Settings.instance().getDbHost() + ":" + 
            //    Settings.instance().getDbPort() + "/";
            String url = "jdbc:sqlite:" + Settings.APP_CONFIG_DIR + 
                    Settings.DIR_SEP + "YoutubeDownloader.db";
            
            connection = DriverManager.getConnection(url/* + Settings.instance().getDbName(),
                    Settings.instance().getDbUser(),Settings.instance().getDbPass()*/);
            
            return true;
        }
        catch(ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "MySQL driver mendzser nem található!\n" + e.getMessage(),"Adatbázis kezelő!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Sikertelen kapcsolódás az adatbázishoz!\n" + e.getMessage(),"Adatbázis kezelő!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void close(){
        if(connection != null){
            try{
                connection.close();
            }
            catch(SQLException e){}
        }
    }
    
    public void insertVideo(YoutubeURLOpt url) throws NullPointerException, SQLException{
        String query = 
            "INSERT INTO videos (title, youtube_url, dirpath, filesize, videoformat, "
                + "added, download_state, convert_to_mp3, remove_original, duration)" +
            "VALUES(?,?,?,?,?,?,?,?,?,?)";
        
        if(connect()){
        
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, url.title);
            ps.setString(2, url.youtubeURL);
            ps.setString(3, url.getDir());
            ps.setLong(4, url.getFileSize());
            ps.setInt(5, url.videoformat.id);
            ps.setLong(6, new Date().getTime());
            ps.setInt(7, url.getDownloadState());
            ps.setBoolean(8, url.isConvertToMp3());
            ps.setBoolean(9, url.isRemoveOriginal());
            ps.setDouble(10, url.getDuration());

            ps.executeUpdate();

            close();
        }
    }
    
    public void deleteVideo(YoutubeURLOpt url) throws SQLException{
        String query = 
            "DELETE FROM videos " +
            "WHERE youtube_url = ? AND videoformat = ?";
        
        if(connect()){
        
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, url.youtubeURL);
            ps.setInt(2, url.videoformat.id);

            ps.executeUpdate();

            close();
        }
    }
    
    public void setMp3Convert(YoutubeURLOpt url, boolean remove) throws SQLException{
        String query = 
            "UPDATE videos SET convert_to_mp3 = ?" + 
            "WHERE youtube_url = ? AND videoformat = ?";
        
        if(connect()){
        
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setBoolean(1, remove);
            ps.setString(2, url.youtubeURL);
            ps.setInt(3, url.videoformat.id);

            ps.executeUpdate();

            close();
        }
    }
    
    public void setRemoveOriginal(YoutubeURLOpt url, boolean remove) throws SQLException{
        String query = 
            "UPDATE videos SET remove_original = ?" + 
            "WHERE youtube_url = ? AND videoformat = ?";
        
        if(connect()){
        
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setBoolean(1, remove);
            ps.setString(2, url.youtubeURL);
            ps.setInt(3, url.videoformat.id);

            ps.executeUpdate();

            close();
        }
    }
    
    public ArrayList<YoutubeURLOpt> getVideos() throws SQLException, MalformedURLException, IOException, YoutubeInvalidContentException, YoutubeErrorStatusException{
        ArrayList<YoutubeURLOpt> retval = new ArrayList<YoutubeURLOpt>();
        
        if(connect()){
        
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(""
                    + "SELECT title, youtube_url, download_state, dirpath, added,"
                    + "filesize, videoformat, convert_to_mp3, remove_original, duration "
                    + "FROM videos ORDER BY added");

            while(rs.next()){
                YoutubeVideoInfo yvi = new YoutubeVideoInfo(rs.getString("youtube_url"));
                String durl = "";
                for(YoutubeURL tmpUrl : yvi.getURLs()){
                    if(tmpUrl.videoformat.id == rs.getInt("videoformat")){
                        durl = tmpUrl.url;
                    }
                }

                YoutubeURL u = new YoutubeURL(
                        durl, 
                        rs.getString("youtube_url"), 
                        rs.getString("title"), 
                        YoutubeVideoInfo.getFormatByID(rs.getInt("videoformat"))
                );

                YoutubeURLOpt urlopt = new YoutubeURLOpt(
                        u, 
                        rs.getString("dirpath"), 
                        rs.getLong("filesize"), 
                        rs.getDouble("duration")
                );
                
                urlopt.setConvertToMp3(rs.getBoolean("convert_to_mp3"));
                urlopt.setRemoveOriginal(rs.getBoolean("remove_original"));
                urlopt.setDuration(rs.getDouble("duration"));
                
                File f = new File(urlopt.getFullPath());
                
                if(urlopt.isConvertToMp3()){
                    File fMp3 = new File(urlopt.getFullMp3Path());
                    if(fMp3.exists() && f.exists() && f.length() == rs.getLong("filesize")){
                        urlopt.setDownloadState(YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED);
                    }
                    /*else if(urlopt.isRemoveOriginal() && fMp3.exists() && MainForm.getFFMpegDuration(fMp3.getAbsolutePath()) == urlopt.getDuration()){
                        urlopt.setDownloadState(YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED);
                    }*/
                    else{
                        if(f.exists() && f.length() == rs.getLong("filesize")){
                            urlopt.setDownloadState(YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED);
                        }
                        else if(rs.getInt("download_state") == YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADING){
                            urlopt.startDownload();
                        }
                        else{
                            urlopt.setDownloadState(rs.getInt("download_state"));
                        }
                    }
                }
                else{
                    if(f.exists() && f.length() == rs.getLong("filesize")){
                        urlopt.setDownloadState(YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED);
                    }
                    else{
                        if(rs.getInt("download_state") == YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADING){
                            urlopt.startDownload();
                        }
                        else{
                            urlopt.setDownloadState(rs.getInt("download_state"));
                        }
                    }
                }

                retval.add(urlopt);
            }


            close();

            return retval;
        }
        return null;
    }
}
