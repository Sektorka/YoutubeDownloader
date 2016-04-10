package youtubedownloader.forms;

import java.awt.Component;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
//import youtubedownloader.classes.DBManager;
import youtubedownloader.classes.ETableModel;
import youtubedownloader.classes.Functions;
import youtubedownloader.classes.Settings;
import youtubedownloader.classes.YoutubeURL;
import youtubedownloader.classes.YoutubeURLOpt;

public class MainForm extends JFrame implements TableModelListener{
    private ArrayList<YoutubeURLOpt> urls;
    private ETableModel<YoutubeURLOpt> tModel;
    
    private static final int COLUMN_TITLE = 0;
    private static final int COLUMN_SIZE = 1;
    private static final int COLUMN_FORMAT = 2;
    private static final int COLUMN_PROGRESS = 3;
    private static final int COLUMN_SPEED = 4;
    private static final int COLUMN_TIME = 5;
    private static final int COLUMN_STATE = 6;
    private static final int COLUMN_MP3_CONVERT = 7;
    private static final int COLUMN_DELETE_ORIGINAL = 8;
    
    public static final String STATE_PENDING = "Nincs letöltve";
    public static final String STATE_DOWNLOADING = "Letöltés...";
    public static final String STATE_CONVERTING = "Konvertálás mp3-ba...";
    public static final String STATE_COMPLETED = "Kész";
    public static final String STATE_STOPPED = "Leállítva";
    public static final String STATE_ERROR = "Hiba";
    
    public static String FFMPEG_DURATION_PATTERN = "^  Duration: (.*), start(.*)";
    private static MainForm mainform;

    
    @Override
    public void tableChanged(TableModelEvent e) {
        if(e.getType() == TableModelEvent.UPDATE){
            for(int row = e.getFirstRow(); row <= e.getLastRow(); row++){
                if(e.getColumn() == COLUMN_MP3_CONVERT){
                    boolean convert = (Boolean)tModel.getValueAt(row, COLUMN_MP3_CONVERT);
                    YoutubeURLOpt url = tModel.getRowDataAt(row);
                    
                    url.setConvertToMp3(convert);
                    /*try{
                        DBManager.instance().setMp3Convert(url, convert);
                    }
                    catch(SQLException ex){
                        ex.printStackTrace();
                    }*/
                }
                else if(e.getColumn() == COLUMN_DELETE_ORIGINAL){
                    boolean remove = (Boolean)tModel.getValueAt(row, COLUMN_DELETE_ORIGINAL);
                    YoutubeURLOpt url = tModel.getRowDataAt(row);
                    
                    url.setRemoveOriginal(remove);
                    /*try{
                        DBManager.instance().setRemoveOriginal(url, remove);
                    }
                    catch(SQLException ex){
                        ex.printStackTrace();
                    }*/                   
                }
            }
        }
    }
    
    private static class ProgressCellRender extends JProgressBar implements TableCellRenderer{
    
        public ProgressCellRender(){
            setMinimum(0);
            setMaximum(100);
            setValue(0);
            setStringPainted(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.setValue(Integer.parseInt(value.toString()));
            return this;
        }
    }
    
    public static MainForm getInstance(){
        if(mainform == null){
            mainform = new MainForm();
        }
        
        return mainform;
    }
    
    private MainForm(){
        tModel = new ETableModel<YoutubeURLOpt>(
            new Object [][] {},
            new String [] {
                "Videó", "Méret", "Formátum", "Folyamat", "Sebesség", "Idő", "Állapot", "Mp3", "Eredeti törlése"
            }
        ){
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, true
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        
        initComponents();
        
        Settings s = Settings.getInstance();
        
        if(hasProxy()){
            
        }
        
        urls = new ArrayList<YoutubeURLOpt>();
        
        //tableMain.setModel(tModel);
        tModel.addTableModelListener(this);
        
        tableMain.getColumnModel().getColumn(COLUMN_TITLE).setPreferredWidth(350);
        tableMain.getColumnModel().getColumn(COLUMN_PROGRESS).setPreferredWidth(150);
        tableMain.getColumnModel().getColumn(COLUMN_SPEED).setMaxWidth(100);
        tableMain.getColumnModel().getColumn(COLUMN_SIZE).setMaxWidth(100);
        tableMain.getColumnModel().getColumn(COLUMN_TIME).setMaxWidth(100);
        
        tableMain.getColumnModel().getColumn(COLUMN_PROGRESS).setCellRenderer(
            new ProgressCellRender()
        );
        
        updateToolbar();
        
        setIconImage(
            new ImageIcon(getClass().getResource("/Resources/youtube_downloader.png")).getImage()
        );
        
        setLocationRelativeTo(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //YoutubeVideoInfo.setFormats();
        ArrayList<YoutubeURLOpt> tmpUrls = null;
        
        /*try{
            tmpUrls = DBManager.instance().getVideos();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Hiba lépett fel a videók betöltése közben!\n" + e.getMessage(),
                    "Youtube letöltő", JOptionPane.ERROR_MESSAGE);
        }*/
        
        if(tmpUrls != null){
            for(YoutubeURLOpt url : tmpUrls){
                addDownload(url,true);
            }
        }
        
        miOpenMp3.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableMenu = new javax.swing.JPopupMenu();
        miOpen = new javax.swing.JMenuItem();
        miOpenMp3 = new javax.swing.JMenuItem();
        miOpenFolder = new javax.swing.JMenuItem();
        miOpenYoutube = new javax.swing.JMenuItem();
        tbMain = new javax.swing.JToolBar();
        btnDownloadNew = new javax.swing.JButton();
        btnDeleteDownload = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnDownloadStart = new javax.swing.JButton();
        btnDownloadStop = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnSettings = new javax.swing.JButton();
        btnAbout = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        tableScrollPane = new javax.swing.JScrollPane();
        tableMain = new javax.swing.JTable();

        miOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/film.png"))); // NOI18N
        miOpen.setText("Videó lejátszás");
        miOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenActionPerformed(evt);
            }
        });
        tableMenu.add(miOpen);

        miOpenMp3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/music.png"))); // NOI18N
        miOpenMp3.setText("MP3 lejátszás");
        miOpenMp3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenMp3ActionPerformed(evt);
            }
        });
        tableMenu.add(miOpenMp3);

        miOpenFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/folder_go.png"))); // NOI18N
        miOpenFolder.setText("Fájl helyének megnyitása");
        miOpenFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenFolderActionPerformed(evt);
            }
        });
        tableMenu.add(miOpenFolder);

        miOpenYoutube.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/YouTube_16.png"))); // NOI18N
        miOpenYoutube.setText("Megtekintés youtube-on");
        miOpenYoutube.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenYoutubeActionPerformed(evt);
            }
        });
        tableMenu.add(miOpenYoutube);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Youtube videó letöltő");

        tbMain.setFloatable(false);
        tbMain.setRollover(true);

        btnDownloadNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/download_32.png"))); // NOI18N
        btnDownloadNew.setToolTipText("Új videó hozzáadás");
        btnDownloadNew.setFocusable(false);
        btnDownloadNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDownloadNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDownloadNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadNewActionPerformed(evt);
            }
        });
        tbMain.add(btnDownloadNew);

        btnDeleteDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/delete_32.png"))); // NOI18N
        btnDeleteDownload.setToolTipText("Kijelölt videók törlése");
        btnDeleteDownload.setFocusable(false);
        btnDeleteDownload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeleteDownload.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDeleteDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDownloadActionPerformed(evt);
            }
        });
        tbMain.add(btnDeleteDownload);
        tbMain.add(jSeparator1);

        btnDownloadStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/start_32.png"))); // NOI18N
        btnDownloadStart.setToolTipText("Kijelölt videók letöltése");
        btnDownloadStart.setFocusable(false);
        btnDownloadStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDownloadStart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDownloadStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadStartActionPerformed(evt);
            }
        });
        tbMain.add(btnDownloadStart);

        btnDownloadStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/stop_32.png"))); // NOI18N
        btnDownloadStop.setToolTipText("Kijelölt videók letöltésének leállítása");
        btnDownloadStop.setFocusable(false);
        btnDownloadStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDownloadStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDownloadStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadStopActionPerformed(evt);
            }
        });
        tbMain.add(btnDownloadStop);
        tbMain.add(jSeparator2);

        btnSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/settings_32.png"))); // NOI18N
        btnSettings.setToolTipText("Beállítások");
        btnSettings.setFocusable(false);
        btnSettings.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettings.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
            }
        });
        tbMain.add(btnSettings);

        btnAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/info.png"))); // NOI18N
        btnAbout.setToolTipText("Névjegy");
        btnAbout.setFocusable(false);
        btnAbout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAbout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAboutActionPerformed(evt);
            }
        });
        tbMain.add(btnAbout);

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/exit.png"))); // NOI18N
        btnExit.setToolTipText("Kilépés");
        btnExit.setFocusable(false);
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        tbMain.add(btnExit);

        tableMain.setAutoCreateRowSorter(true);
        tableMain.setModel(tModel);
        tableMain.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        tableMain.setRowHeight(20);
        tableMain.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tableMain.getTableHeader().setReorderingAllowed(false);
        tableMain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMainMouseClicked(evt);
            }
        });
        tableScrollPane.setViewportView(tableMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tbMain, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDownloadNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadNewActionPerformed
        NewDownloadForm ndf = NewDownloadForm.getInstance(this, true);
        
        if(ndf.Show() == NewDownloadForm.RESULT_OK){
            addDownload(new YoutubeURLOpt(ndf.getURL(),Settings.getInstance().getStrDownloadPath()));
            ndf.dispose();
        }
        
    }//GEN-LAST:event_btnDownloadNewActionPerformed

    private void btnDeleteDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDownloadActionPerformed
        deleteDownloads();
    }//GEN-LAST:event_btnDeleteDownloadActionPerformed

    private void btnDownloadStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadStartActionPerformed
        startDownloads();
    }//GEN-LAST:event_btnDownloadStartActionPerformed

    private void btnDownloadStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadStopActionPerformed
        stopDownloads();
    }//GEN-LAST:event_btnDownloadStopActionPerformed

    private void tableMainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMainMouseClicked
        if(evt.getButton() == java.awt.event.MouseEvent.BUTTON1){
            if(evt.getClickCount() == 1){
                updateToolbar();
            }
            else if(evt.getClickCount() > 1){
                switch(Settings.getInstance().getDefualtOperation()){
                    case Settings.DEFAULT_OPERATION_OPEN:
                        playVideos();
                        break;
                    case Settings.DEFAULT_OPERATION_OPEN_CONTAIN_DIR:
                        openDownloadsFolder();
                        break;
                    default:
                        openYoutubes();
                        break;
                }
            }
        }
        else if(evt.getButton() == java.awt.event.MouseEvent.BUTTON3){
            int rowNumber = tableMain.rowAtPoint(evt.getPoint());
            tableMain.getSelectionModel().setSelectionInterval( rowNumber, rowNumber );
            
            updateTableMenu();
            tableMenu.show(tableMain, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tableMainMouseClicked

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        new AboutForm(this,true).setVisible(true);
    }//GEN-LAST:event_btnAboutActionPerformed

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        new SettingsForm(this,true).setVisible(true);
    }//GEN-LAST:event_btnSettingsActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void miOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenActionPerformed
        playVideos();
    }//GEN-LAST:event_miOpenActionPerformed

    private void miOpenFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenFolderActionPerformed
        openDownloadsFolder();
    }//GEN-LAST:event_miOpenFolderActionPerformed

    private void miOpenYoutubeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenYoutubeActionPerformed
        openYoutubes();
    }//GEN-LAST:event_miOpenYoutubeActionPerformed

    private void miOpenMp3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenMp3ActionPerformed
        playMp3();
    }//GEN-LAST:event_miOpenMp3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbout;
    private javax.swing.JButton btnDeleteDownload;
    private javax.swing.JButton btnDownloadNew;
    private javax.swing.JButton btnDownloadStart;
    private javax.swing.JButton btnDownloadStop;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnSettings;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JMenuItem miOpen;
    private javax.swing.JMenuItem miOpenFolder;
    private javax.swing.JMenuItem miOpenMp3;
    private javax.swing.JMenuItem miOpenYoutube;
    private javax.swing.JTable tableMain;
    private javax.swing.JPopupMenu tableMenu;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JToolBar tbMain;
    // End of variables declaration//GEN-END:variables

    private void addDownload(YoutubeURLOpt url){
        addDownload(url, false);
    }
    
    private void addDownload(YoutubeURLOpt url, boolean ignoreInsert) {
        if(url == null) return;
        
        urls.add(url);
        
        Object[] row = new Object[9];
        //Vector row = new Vector();
        
        int percent = 0;
        
        /*if(url.isConvertToMp3() && url.isRemoveOriginal()){
            double mp3Length = -1;
            try {
                mp3Length = getFFMpegDuration(url.getFullMp3Path());
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            if(mp3Length == url.getDuration()){
                percent = 100;
            }
        }
        else{
            percent = (url.getFileSize() == 0 ? 0 : (int)(url.getCurrentFileSize() * 100 / url.getFileSize()));
        }
        
        

        if(percent > 100){
            percent = 100;
        }*/
        
        row[0] = url.title;
        row[1] = Functions.formatBytes(url.getFileSize());
        row[2] = url.videoformat.desc;
        row[3] = percent;
        row[4] = "0,00 B/s";
        row[5] = "--:--";
        row[6] = (percent == 100 ? STATE_COMPLETED : STATE_PENDING);
        row[7] = url.isConvertToMp3();
        row[8] = url.isRemoveOriginal();
        
        if(percent == 100){
            url.setDownloadState(YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED);
        }
        
        tModel.addRow(url, row);
        
        if(url.getDownloadState() != YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED && Settings.getInstance().isbAutoDownload()){
            url.startDownload();
        }
        
        updateToolbar();
        
        /*if(!ignoreInsert){
            try{
                DBManager.instance().insertVideo(url);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, "Hiba lépett fel a videó mentése közben!\n" + e.getMessage(),
                    "Youtube letöltő",JOptionPane.WARNING_MESSAGE);
            }
        }*/
    }
    
    private void playVideos(){
        for(int row : tableMain.getSelectedRows()){
            if(urls.get(row).getDownloadState() == YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED){
                try {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.open(new File(urls.get(row).getFullPath()));
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Ezen a számítógépen nem található média lejátszó!",
                            "Youtube letöltő",JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Hiba lépett fel a videó megnyitása közben!\n" + ex.getMessage(),
                            "Youtube letöltő",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void playMp3(){
        for(int row : tableMain.getSelectedRows()){
            if(urls.get(row).getDownloadState() == YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED && 
                    urls.get(row).isConvertToMp3()){
                try {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.open(new File(urls.get(row).getFullMp3Path()));
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Ezen a számítógépen nem található média lejátszó!",
                            "Youtube letöltő",JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Hiba lépett fel az MP3 megnyitása közben!\n" + ex.getMessage(),
                            "Youtube letöltő",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void openDownloadsFolder(){
        for(int row : tableMain.getSelectedRows()){
            try {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.open(new File(urls.get(row).getDir()));
                }
                else{
                    JOptionPane.showMessageDialog(this, "Ezen a számítógépen nem található fájl böngésző!",
                        "Youtube letöltő",JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Hiba lépett fel a mappa megnyitása közben!\n" + ex.getMessage(),
                        "Youtube letöltő",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openYoutubes(){
        for(int row : tableMain.getSelectedRows()){
            try {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(urls.get(row).youtubeURL));
                }
                else{
                    JOptionPane.showMessageDialog(this, "Nincs böngésző amivel meglehet nyitni a youtube linket!",
                        "Youtube letöltő",JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hiba lépett fel az oldal megnyitása közben!\n" + ex.getMessage(),
                        "Youtube letöltő",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startDownloads() {
        for(int row : tableMain.getSelectedRows()){
            urls.get(row).startDownload();
        }
        updateToolbar();
    }
    
    private void stopDownloads() {
        for(int row : tableMain.getSelectedRows()){
            urls.get(row).stopDownload();
        }
        updateToolbar();
    }
    
    private void deleteDownloads() {
        if(tableMain.getSelectedRowCount() == 0) return;
        
        String videos = "";
        
        for(int row : tableMain.getSelectedRows()){
            videos += "    - " + tModel.getValueAt(row, COLUMN_TITLE).toString() + "\n";
        }
        
        if(
           JOptionPane.showConfirmDialog(this, 
                "Szeretné töröli a következő letöltéseket?\n" + videos,
                "Letöltés törlés", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE
           ) == JOptionPane.YES_OPTION
        )
        {
            int[] selectedRows = tableMain.getSelectedRows();
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int row = selectedRows[i];
                if(urls.get(row).getDownloadState() == YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADING){
                    urls.get(row).stopDownload();
                }
                
                if(urls.get(row).getDownloadState() != YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED && 
                        Settings.getInstance().isbDeleteUnfinisedVideoFile()){
                    try{
                        new File(urls.get(row).getFullPath()).delete();
                    }
                    catch(Exception e){}
                }
                
                /*try{
                    DBManager.instance().deleteVideo(urls.get(row));
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(this, "Hiba lépett fel a videó törlése közben!\n" + e.getMessage(),
                        "Youtube letöltő",JOptionPane.WARNING_MESSAGE);
                }*/
                
                urls.remove(row);
                tModel.removeRow(row);
            }
        }
        updateToolbar();
    }
    
    private void setEnabledToolbar(boolean enabled){
        btnDownloadStart.setEnabled(enabled);
        btnDownloadStop.setEnabled(enabled);
        btnDeleteDownload.setEnabled(enabled);
    }
    
    public void updateToolbar(){
        if(tableMain.getSelectedRowCount() == 1){
            btnDeleteDownload.setEnabled(true);
            
            switch(urls.get(tableMain.getSelectedRow()).getDownloadState()){
                case YoutubeURLOpt.DOWNLOAD_STATE_PENDING:
                    btnDownloadStart.setEnabled(true);
                    btnDownloadStop.setEnabled(false);
                    break;
                case YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADING:
                    btnDownloadStart.setEnabled(false);
                    btnDownloadStop.setEnabled(true);
                    break;
                case YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED:
                    btnDownloadStart.setEnabled(false);
                    btnDownloadStop.setEnabled(false);
                    break;
                case YoutubeURLOpt.DOWNLOAD_STATE_STOPPED:
                    btnDownloadStart.setEnabled(true);
                    btnDownloadStop.setEnabled(false);
                    break;
                default:
                    btnDownloadStart.setEnabled(false);
                    btnDownloadStop.setEnabled(false);
                    break;
            }
            
            
        }
        else if(tableMain.getSelectedRowCount() > 1){
            setEnabledToolbar(true);
        }
        else{
            setEnabledToolbar(false);
        }
    }
    
    private int getUrlIndex(YoutubeURLOpt url){
        int index = -1;
        
        for(int i = 0; i < urls.size(); i++){
            if(urls.get(i) == url){
                index = i;
            }
        }
        
        return index;
    }
    
    public void setSize(YoutubeURLOpt url){
        int index = getUrlIndex(url);
        
        if(index != -1){
            tModel.setValueAt(Functions.formatBytes(url.getFileSize()), index, COLUMN_SIZE);
        }
    }
    
    public void setStatus(YoutubeURLOpt url, String status){
        int index = getUrlIndex(url);
        
        if(index != -1){
            tModel.setValueAt(status, index, COLUMN_STATE);
        }
    }
    
    public void setProgess(YoutubeURLOpt url, int percent){
        int index = getUrlIndex(url);
        
        if(index != -1){
            tModel.setValueAt(percent, index, COLUMN_PROGRESS);
        }
    }
    
    public void setSpeed(YoutubeURLOpt url, long speed){
        int index = getUrlIndex(url);
        
        if(index != -1){
            tModel.setValueAt(Functions.formatBytes(speed) + "/s", index, COLUMN_SPEED);
        }
    }
    
    public void setRemaingTime(YoutubeURLOpt url, long seconds){
        setRemaingTime(url, seconds, false);
    }
    
    public void setRemaingTime(YoutubeURLOpt url, long seconds, boolean done){
        int index = getUrlIndex(url);
        
        if(index != -1){
            if(done){
                tModel.setValueAt("--:--", index, COLUMN_TIME);
            }
            else{
                tModel.setValueAt(Functions.formatSeconds(seconds), index, COLUMN_TIME);
            }
        }
    }
    
    public boolean urlExists(YoutubeURL url){
        for(YoutubeURL u : urls){
            if(u.equals(url)){
                return true;
            }
        }
        return false;
    }

    private void updateTableMenu() {
        int totalNotDownloaded = 0, totalMP3 = 0;
        for(int row : tableMain.getSelectedRows()){
            if(urls.get(row).getDownloadState() != YoutubeURLOpt.DOWNLOAD_STATE_DOWNLOADED){
                totalNotDownloaded++;
            }
            else{
                if(urls.get(row).isConvertToMp3()){
                    totalMP3++;
                }
            }
        }
        
        miOpen.setEnabled(totalNotDownloaded != tableMain.getSelectedRowCount());
        miOpenMp3.setVisible(totalMP3 == tableMain.getSelectedRowCount());
        
    }
    
    public static double getFFMpegDuration(String mediaFile) throws IOException, InterruptedException{
        String getMp3LengthCommand = (Settings.isWindows() ? "cmd /c " : "")
                + Settings.getInstance().getFfmpegPath() + " -i " + 
                mediaFile;
        
        String line;
        double retval = 0;
        
        Process p = Runtime.getRuntime().exec(getMp3LengthCommand);

        BufferedReader bre = new BufferedReader
            (new InputStreamReader(p.getErrorStream()));

        while ((line = bre.readLine()) != null){
            Matcher matcher = Pattern.compile(FFMPEG_DURATION_PATTERN).matcher(line);
            if(matcher.matches()){
                retval = Functions.getSeconds(matcher.group(1));
                break;
            }
        }
        
        bre.close();
        p.waitFor();
        
        return retval;
    }
    
    public static boolean hasProxy(){
        final String TEST_URL = "http://google.com/";
        
        try {
            URL url = new URL(TEST_URL);           
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            int responseCode = connection.getResponseCode();
            
            if(responseCode == 407){
                return true;
            }
            
            return false;
        } catch (SocketTimeoutException ex){
            return true;
        } catch (IOException ex) {
            //ex.printStackTrace();
            return false;   
        }
    }
}

