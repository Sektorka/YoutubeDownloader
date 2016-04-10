package youtubedownloader.classes;

import java.text.DecimalFormat;

public final class Functions {
    private Functions(){}
    
    public static String formatBytes(double bytes){
        if (bytes < 1000)
            return numberFormat(bytes,2) + " B";
        else if (bytes < 1000 * 1024)
            return numberFormat(bytes / 1024,2) + " KB";
        else if (bytes < 1000 * 1048576)
            return numberFormat(bytes / 1048576,2) + " MB";
        else if (bytes < 1000 * 1073741824L)
            return numberFormat(bytes / 1073741824,2) + " GB";
        else if (bytes < 1000 * 1099511627776L)
            return numberFormat(bytes / 1099511627776L,2) + " TB";
        return "0 B";
    }
    
    public static String numberFormat(double number, int decimal){
        return (new DecimalFormat("0.00")).format(number);
    }
    
    public static String formatSeconds(long s){
        int h, m;
        
        if(s < 0) s = 0;
        
        h = (int)(s / 3600);
        s %= 3600;

        m = (int)(s / 60);
        s %= 60;

        return (h > 0 ? (h < 10 ? "0" + h : h) + ":" : "") + 
               (m < 10 ? "0" + m : m) + ":" + 
               (s < 10 ? "0" + s : s);
    }
    
    public static double getSeconds(String time){
        String[] arrSlices = time.split(":");
        
        if(arrSlices.length == 3){
            return (Double.parseDouble(arrSlices[0]) * 3600 + 
                    Double.parseDouble(arrSlices[1]) * 60 + 
                    Double.parseDouble(arrSlices[2])
            );
        }
        else{
            return 0;
        }
    }
}
