package youtubedownloader.classes;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Authenticator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import youtubedownloader.forms.MainForm;

public class Settings {
    private static Settings settings;
    
    public static final String DIR_SEP = System.getProperty("file.separator");
    public static final String HOME = System.getProperty("user.home");
    public static final String APP_CONFIG_DIR = 
            System.getProperty("user.home") + (HOME.charAt(HOME.length()-1) != HOME.charAt(0) ? DIR_SEP : "") +
            (!Settings.isWindows() ? ".config" : "") + 
            DIR_SEP + "YoutubeDownloader";
    
    public static final String APP_NAME = "Youtube videó letöltő";
    public static final String VERSION = "1.1.1";
    public static final String CONTACT = "krisztisektor@gmail.com";
    
    private static final String FILE = APP_CONFIG_DIR + 
            DIR_SEP + "YoutubeDownloader.xml";
    private static final String FILE_PROT = "file:///" + FILE;
    
    public static final int DEFAULT_OPERATION_OPEN = 0;
    public static final int DEFAULT_OPERATION_OPEN_CONTAIN_DIR = 1;
    public static final int DEFAULT_OPERATION_WATCH_IN_YOUTUBE = 2;
    
    private String dbHost, dbUser, dbPass, dbName, strDownloadPath, 
            replaceKeyWord, proxyHost, proxyUser, proxyPass, ffmpegPath,
            appTheme;
    private int dbPort, defualtOperation, proxyPort;
    private boolean bAutoDownload, bDeleteUnfinisedVideoFile, bUseProxy;
    
    static{
        File app_conf_dir = new File(APP_CONFIG_DIR);
        
        if(!app_conf_dir.exists() || !app_conf_dir.isDirectory()){
            app_conf_dir.mkdirs();
        }
    }

    public static Settings getInstance(){
        if(settings == null){
            settings = new Settings();
        }
        
        return settings;
    }
    
    private Settings(){
        dbHost = dbUser = dbPass = dbName = 
        proxyHost = proxyUser = proxyPass = ffmpegPath = "";
        appTheme = "Nimbus";
        strDownloadPath = System.getProperty("user.home");
        replaceKeyWord = "_";
        dbPort = 3306;
        proxyPort = 3128;
        bAutoDownload = bDeleteUnfinisedVideoFile = true;
        bUseProxy = false;
        
        try{
            loadSettings();
        }
        catch(FileNotFoundException e){
            try{
                saveSettings();
            }
            catch(Exception ex){
                ex.printStackTrace();
                //JOptionPane.showMessageDialog(MainForm.getInstance(), "Hiba lépett fel a konfigurációs fájl létrehozása közben!\r\n" + ex.getMessage(),"Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, "Hiba lépett fel a " + FILE + " konfigurációs fájl beolvasása közben!\n" + e.getMessage(),"Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
        catch(YoutubeSettingsException e){
            JOptionPane.showMessageDialog(null, e.getMessage(),"Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(MainForm.getInstance(), "A \"" + FILE + "\" konfigurációs fájl hibás értékeket tartalmaz!","Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(MainForm.getInstance(), "Hiba történt a " + FILE + " konfigurációs fájl feldolgozása közben!","Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Field getFieldByName(String fieldName){
        for(Field field : this.getClass().getDeclaredFields()){
            if(field.getName().equals(fieldName)){
                return field;
            }
        }
        
        return null;
    }
    
    private void loadSettings() throws FileNotFoundException, IOException, 
            ParserConfigurationException, SAXException, YoutubeSettingsException, 
            IllegalArgumentException, IllegalAccessException, NumberFormatException{
        
        File f = new File(FILE);
        
        if(!f.exists()){
            throw new FileNotFoundException(FILE);
        }
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        InputStreamReader in = new InputStreamReader(new FileInputStream(FILE), "utf-8" );

        BufferedReader reader = new BufferedReader ( in );

        InputSource input = new InputSource(reader);

        //Element records = builder.parse(input).getDocumentElement();
        
        //Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(FILE_PROT);
        Element root = builder.parse(input).getDocumentElement();
        
        /*NodeList rootnl = d.getElementsByTagName("YoutubeDownloader");
        if(rootnl.getLength() != 1){
            throw new YoutubeSettingsException("Hibás a beállításokat tartalmazó xml fájl!");
        }
        
        Element root = (Element) rootnl.item(0);*/
        
        NodeList fields = root.getChildNodes();
        
        for(int i = 0; i < fields.getLength(); i++){
            if(fields.item(i) != null){
                Field field = getFieldByName(fields.item(i).getNodeName());
                String nodeValue = "";
                
                if(fields.item(i).getFirstChild() != null){
                    nodeValue = fields.item(i).getFirstChild().getNodeValue();
                }
                
                if(field != null){
                    if(field.getType().getName().equals("java.lang.String")){
                        field.set(this, nodeValue);
                    }
                    else if(field.getType().getName().equals("int")){
                        field.set(this, Integer.parseInt(nodeValue));
                    }
                    else if(field.getType().getName().equals("boolean")){
                        field.set(this, Boolean.parseBoolean(nodeValue));
                    }
                }
            }
            
        }
        
        if(dbPort <= 0 || dbPort > 65535){
            dbPort = 3306;
        }
        
        File path = new File(strDownloadPath);
        if(!path.exists() || !path.isDirectory()){
            strDownloadPath = "";
        }
        
        updateProxyConfig();
    }
    
    public void saveSettings() throws ParserConfigurationException, IllegalArgumentException, 
            IllegalAccessException, IOException{
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("YoutubeDownloader");
        doc.appendChild(root);
        
        Field[] fields = this.getClass().getDeclaredFields();
        
        for(Field field : this.getClass().getDeclaredFields()){
            if(Modifier.isStatic(field.getModifiers())) continue;
            
            Element fieldElement = doc.createElement(field.getName());
            Object value = field.get(this);
            fieldElement.setTextContent(value != null ? value.toString() : "");
            root.appendChild(fieldElement);
        }
        
        OutputFormat format = new OutputFormat(doc);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(4);
        
        Writer out = new StringWriter();
        
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(doc);
        
        BufferedWriter bwriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(FILE), "UTF-8"));
        
        bwriter.write(out.toString());
        
        bwriter.close();
        out.close();
        
        updateProxyConfig();
    }

    public String getReplaceKeyWord() {
        return replaceKeyWord;
    }

    public void setReplaceKeyWord(String replaceKeyWord) throws YoutubeSettingsException {
        String notAllowed = "\\/?:*\"><|";
        for(int i = 0; i < notAllowed.length(); i++){
            if(replaceKeyWord.contains("" + notAllowed.charAt(i))){
                throw new YoutubeSettingsException("Nem elfogadható karakter " + notAllowed.charAt(i) + " lecserélendő karakternek!");
                        
            }
        }
        this.replaceKeyWord = replaceKeyWord;
    }
    
    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getStrDownloadPath() {
        return strDownloadPath;
    }

    public void setStrDownloadPath(String strDownloadPath) throws YoutubeSettingsException {
        File dir = new File(strDownloadPath);
        
        if(!dir.exists()){
            throw new YoutubeSettingsException("A \"" + strDownloadPath + "\" mappa nem létezik! Kérem tallózza be a letöltések helyének mappáját!");
        }
        
        if(!dir.isDirectory()){
            throw new YoutubeSettingsException("A \"" + strDownloadPath + "\" nem mappa! Kérem tallózza be a letöltések helyének mappáját!");
        }
        
        this.strDownloadPath = strDownloadPath;
    }

    public int getDbPort() {
        if(dbPort <= 0 || dbPort > 65535){
            return dbPort;
        }
        else{
            return 3306;
        }
        
    }

    public void setDbPort(int dbPort) throws YoutubeSettingsException {
        if(dbPort <= 0 || dbPort > 65535){
            throw new YoutubeSettingsException("Hibás portot adtál meg! (" + dbPort + ")\n" +
                    "A portszámnak 1 és 65535 között kell lennie!"
                    );
        }
        
        this.dbPort = dbPort;
    }

    public boolean isbAutoDownload() {
        return bAutoDownload;
    }

    public void setbAutoDownload(boolean bAutoDownload) {
        this.bAutoDownload = bAutoDownload;
    }

    public boolean isbDeleteUnfinisedVideoFile() {
        return bDeleteUnfinisedVideoFile;
    }

    public void setbDeleteUnfinisedVideoFile(boolean bDeleteUnfinisedVideoFile) {
        this.bDeleteUnfinisedVideoFile = bDeleteUnfinisedVideoFile;
    }
    
    public int getDefualtOperation() {
        return defualtOperation;
    }

    public void setDefualtOperation(int defualtOperation) throws YoutubeSettingsException {
        if(defualtOperation < DEFAULT_OPERATION_OPEN || 
                defualtOperation > DEFAULT_OPERATION_WATCH_IN_YOUTUBE){
            throw new YoutubeSettingsException("Hibás alapértelmezett műveletett próbálsz beállítani!");
        }
        
        this.defualtOperation = defualtOperation;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPass() {
        return proxyPass;
    }

    public void setProxyPass(String proxyPass) {
        this.proxyPass = proxyPass;
    }

    public int getProxyPort() {
        if(proxyPort <= 0 || proxyPort > 65535){
            return proxyPort;
        }
        else{
            return 3128;
        }
    }

    public void setProxyPort(int proxyPort) throws YoutubeSettingsException {
        if(proxyPort <= 0 || proxyPort > 65535){
            throw new YoutubeSettingsException("Hibás proxy portot adtál meg! (" + proxyPort + ")\n" +
                    "A portszámnak 1 és 65535 között kell lennie!"
                    );
        }
        
        this.proxyPort = proxyPort;
    }

    public String getFfmpegPath() {
        if(ffmpegPath == null){
            return (isWindows() ? "ffmpeg.exe" : "./ffmpeg");
        }
        
        return ffmpegPath;
    }

    public void setFfmpegPath(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
    }
    
    public static boolean isWindows(){
        return System.getProperty("os.name").startsWith("Windows");
    }

    public String getAppTheme() {
        return (appTheme == null ? UIManager.getLookAndFeel().getName() : appTheme);
    }

    public void setAppTheme(String appTheme) {
        this.appTheme = appTheme;
    }

    public boolean isUseProxy() {
        return bUseProxy;
    }

    public void setUseProxy(boolean bUseProxy) {
        this.bUseProxy = bUseProxy;
    }
    
    public void updateProxyConfig(){
        if(isUseProxy()){
            System.setProperty("http.proxyHost", getProxyHost());
            System.setProperty("http.proxyPort", getProxyPort() + "");
            System.setProperty("http.proxyUser", getProxyUser());
            System.setProperty("http.proxyPassword", getProxyPass());

            System.setProperty("https.proxyHost", getProxyHost());
            System.setProperty("https.proxyPort", getProxyPort() + "");
            System.setProperty("https.proxyUser", getProxyUser());
            System.setProperty("https.proxyPassword", getProxyPass());
            
            Authenticator.setDefault(new ProxyAuth(getProxyUser(), getProxyPass()));
        }
        else{
            System.setProperty("http.proxyHost", "");
            System.setProperty("http.proxyPort", "");
            System.setProperty("http.proxyUser", "");
            System.setProperty("http.proxyPassword", "");

            System.setProperty("https.proxyHost", "");
            System.setProperty("https.proxyPort", "");
            System.setProperty("https.proxyUser", "");
            System.setProperty("https.proxyPassword", "");
            
            Authenticator.setDefault(null);
        }
    }
}