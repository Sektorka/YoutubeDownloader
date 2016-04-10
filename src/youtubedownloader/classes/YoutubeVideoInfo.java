package youtubedownloader.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;
import youtubedownloader.forms.NewDownloadForm;

public class YoutubeVideoInfo {
    private String url;
    
    private static final String VIDEO_INFO_PAGE_URL = "http://www.youtube.com/get_video_info?&video_id=%s&el=detailpage&ps=default&eurl=&gl=%s&hl=%s";
    private static final String USER_AGENT = "Youtube downloader v1.0";
    private static final String VALID_URL_PATTERN = "^http(s|):\\/\\/(www\\.|)youtube\\.com\\/watch\\?(.*)v=(.*)";
    
    public static final YoutubeVideoFormat[] FORMATS = {
        new YoutubeVideoFormat(13,   16, 0,     "3pg",  "3gp (N/A)",           false),
        new YoutubeVideoFormat(17,   24, 144,   "3gp",  "3gp (144p)",          false),
        new YoutubeVideoFormat(36,   38, 240,   "3gp",  "3gp (240p)",          false),

        new YoutubeVideoFormat(43,   128, 360,  "webm", "WebM (360p)",         false),
        new YoutubeVideoFormat(44,   128, 480,  "webm", "WebM (480p)",         false),
        new YoutubeVideoFormat(45,   192, 720,  "webm", "WebM (720p)",         false),
        new YoutubeVideoFormat(46,   192, 1080, "webm", "WebM (1080p)",        false),
        new YoutubeVideoFormat(100,  128, 360,  "webm", "WebM (360p) 3D",      true),
        new YoutubeVideoFormat(101,  192, 360,  "webm", "WebM (360p) 3D",      true),
        new YoutubeVideoFormat(102,  192, 720,  "webm", "WebM (720p) 3D",      true),

        new YoutubeVideoFormat(5,    64,  240,  "flv",  "Flv (240p)",          false),
        new YoutubeVideoFormat(6,    64,  270,  "flv",  "Flv (270p)",          false),
        new YoutubeVideoFormat(34,   64,  320,  "flv",  "Flv (320p)",          false),
        new YoutubeVideoFormat(35,   64,  480,  "flv",  "Flv (480p)",          false),
        new YoutubeVideoFormat(120,  64,  720,  "flv",  "Flv (720p)",          false),

        new YoutubeVideoFormat(160,  64,  144,  "mp4", "Mp4 (144p) Adaptive",  false),
        new YoutubeVideoFormat(139,  96,  0,    "mp4", "Mp4 (N/Ap) Adaptive",  false),
        new YoutubeVideoFormat(140,  96,  0,    "mp4", "Mp4 (N/Ap) Adaptive",  false),
        new YoutubeVideoFormat(141,  96,  0,    "mp4", "Mp4 (N/Ap) Adaptive",  false),
        new YoutubeVideoFormat(133,  96,  240,  "mp4", "Mp4 (240p) Adaptive",  false),
        new YoutubeVideoFormat(83,   96,  240,  "mp4", "Mp4 (240p) 3D",        true),
        new YoutubeVideoFormat(134,  96,  360,  "mp4", "Mp4 (360p) Adaptive",  false),
        new YoutubeVideoFormat(82,   96,  360,  "mp4", "Mp4 (360p) 3D",        true),
        new YoutubeVideoFormat(135,  96,  480,  "mp4", "Mp4 (480p) Adaptive",  false),
        new YoutubeVideoFormat(18,   96,  480,  "mp4", "Mp4 (480p)",           false),
        new YoutubeVideoFormat(85,   152, 520,  "mp4", "Mp4 (520p) 3D",        false),
        new YoutubeVideoFormat(136,  192, 720,  "mp4", "Mp4 (720p) Adaptive",  false),
        new YoutubeVideoFormat(22,   192, 720,  "mp4", "Mp4 (720p)",           false ),
        new YoutubeVideoFormat(84,   152, 720,  "mp4", "Mp4 (720p) 3D",        true),
        new YoutubeVideoFormat(137,  192, 1080, "mp4", "Mp4 (1080p) Adaptive", false),
        new YoutubeVideoFormat(37,   192, 1080, "mp4", "Mp4 (1080p) 3D",       true),
        new YoutubeVideoFormat(38,   192, 3072, "mp4", "Mp4 (3072p) 3D",       true)
    };
    
    public YoutubeVideoInfo(String url){
        this.url = url;
    }
    
    public ArrayList<YoutubeURL> getURLs() throws MalformedURLException, IOException, YoutubeInvalidContentException, YoutubeErrorStatusException{
        String content = getContent();
        
        if(content.equals("")){
            throw new YoutubeInvalidContentException("Lekérdezett tartalom üres!");
        }
        
        HashMap<String, String> kv = parseQuery(content);
        
        if(!kv.containsKey("status")){
            throw new YoutubeInvalidContentException("\"status\" paraméter nem található a lekérdezett tartalomban!");
        }

        if(!kv.get("status").equals("ok")){
            if(!kv.containsKey("reason")){
                throw new YoutubeErrorStatusException("Nincs indok!");
            }
            else{
                throw new YoutubeErrorStatusException(URLDecoder.decode(kv.get("reason"), "UTF-8"));
            }
        }
        
        if(!kv.containsKey("title")){
            throw new YoutubeInvalidContentException("\"title\" paraméter nem található a lekérdezett tartalomban!");
        }
        
        if(!kv.containsKey("url_encoded_fmt_stream_map")){
            throw new YoutubeInvalidContentException("\"url_encoded_fmt_stream_map\" paraméter nem található a lekérdezett tartalomban!");
        }

        SortedMap<Integer,YoutubeURL> Urls = new TreeMap<Integer,YoutubeURL>();
        
        String strUrls = URLDecoder.decode(kv.get("url_encoded_fmt_stream_map"),"UTF-8");
        String[] formats = strUrls.split(",");
        
        ArrayList<String> testUrls = new ArrayList<String>();
        
        
        for(String format : formats){
            HashMap<String, String> kvFormat = parseQuery(format);

            if(!kvFormat.containsKey("url")){
                continue;
            }
            
            /*{
                String sign = null;
                
                if(kvFormat.containsKey("s")){
                    sign = kvFormat.get("s");
                } else if(kvFormat.containsKey("sig")){
                    sign = kvFormat.get("sig");
                } else if(kvFormat.containsKey("signature")){
                    sign = kvFormat.get("signature");
                }
                
                
                if(sign == null || !kvFormat.containsKey("itag") ||
                    !kvFormat.containsKey("url") || !kvFormat.containsKey("fallback_host")){
                    continue;
                    //throw new YoutubeInvalidContentException("\"url\" paraméter nem található a lekérdezett tartalomban!");
                }
                
                

                String url = String.format("%s&fallback_host=%s&signature=%s", 
                                kvFormat.get("url"), 
                                kvFormat.get("fallback_host"), 
                                sign);
                
                testUrls.add(url);
            }*/

            String url = URLDecoder.decode(kvFormat.get("url"),"UTF-8") +
                    "&fallback_host=" + URLDecoder.decode(kvFormat.get("fallback_host"),"UTF-8");
            
            if(!url.contains("signature")){
                String sign = null;
            
                if(kvFormat.containsKey("s")){
                    sign = kvFormat.get("s");
                } else if(kvFormat.containsKey("sig")){
                    sign = kvFormat.get("sig");
                } else if(kvFormat.containsKey("signature")){
                    sign = kvFormat.get("signature");
                }
                
                url += "&signature=" + sign;
            }
            
            System.out.println(url);

            YoutubeVideoFormat vformat = getFormatByID( Integer.parseInt(kvFormat.get("itag")) );
            
            if(vformat != null)
                Urls.put(vformat.sort, new YoutubeURL(url, this.url, URLDecoder.decode(kv.get("title"), "UTF-8"), vformat));
        }
        
        for(String tu : testUrls){
            System.out.println(tu);
        }

        ArrayList<YoutubeURL> retval = new ArrayList<YoutubeURL>();
        
        int counter = 0;
        Iterator<Integer> iterator = Urls.keySet().iterator();

        while (iterator.hasNext()) {
            retval.add(Urls.get(iterator.next()));
        }
        
        return retval;
    }
    
    public static YoutubeVideoFormat getFormatByID(int id){
        for(YoutubeVideoFormat f : FORMATS){
            if(f.id == id){
                return f;
            }
        }
        return null;
    }
    
    private String getContent() throws MalformedURLException, IOException{
        HttpURLConnection connection;
        
        connection = (HttpURLConnection) getInfoURL().openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(NewDownloadForm.CONNECTION_TIMEOUT);
        
        /*Settings s = Settings.getInstance();
        
        if(s.isUseProxy()){
            String encoded = new String(Base64.getEncoder().encode((s.getProxyUser() + ":" + s.getProxyPass()).getBytes()));
            connection.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
            Authenticator.setDefault(new ProxyAuth(s.getProxyUser(), s.getProxyPass()));
        }*/
        
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream(), "UTF-8")
        );

        String buffer, content = "";
    	
    	while((buffer = in.readLine()) != null)
    		content += buffer;
    	
    	in.close();
    	
    	return content;
    }
    
    private URL getInfoURL() throws MalformedURLException{
        String query = URI.create(url).getQuery();
        String vParam = "";
        
        for(String kv : query.split("&")){
            String[] keyVal = kv.split("=");
            
            if(keyVal.length == 2 && keyVal[0].equals("v")){
                vParam = keyVal[1];
            }
        }
        
        return new URL(String.format(VIDEO_INFO_PAGE_URL, vParam, "HU", "hu"));
    }
    
    public static boolean isValidURL(String url){
        return Pattern.compile(VALID_URL_PATTERN).matcher(url).matches();
    }
    
    public static HashMap<String, String> parseQuery(String query){
        HashMap<String, String> retval = new HashMap<String, String>();

        String[] keyVals = query.split("&");
        
        for(String kv : keyVals){
            String[] akv = kv.split("=");
            if(akv.length == 2){
                retval.put(akv[0], akv[1]);
            }
            else{
                retval.put(akv[0], "");
            }
        }

        return retval;
    }
}
