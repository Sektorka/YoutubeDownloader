package youtubedownloader.forms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import youtubedownloader.classes.Settings;
import youtubedownloader.classes.YoutubeErrorStatusException;
import youtubedownloader.classes.YoutubeInvalidContentException;
import youtubedownloader.classes.YoutubeURL;
import youtubedownloader.classes.YoutubeVideoInfo;

public class NewDownloadForm extends javax.swing.JDialog {
    private Timer timer, timeoutTimer;
    private DefaultComboBoxModel cbModel;
    private ArrayList<YoutubeURL> urls;
    private YoutubeURL url;
    private int result;
    public static boolean qualityDetectAborted = false;
    private static NewDownloadForm instance = null;
    
    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCEL = 0;
    
    public static final int CONNECTION_TIMEOUT = 5; //second
    
    private static final String ERR_INVALID_URL = "Érvénytelen URL!";
    private static final String ERR_ALREADY_EXISTS = "Ez a videó már a letöltési listában van!";
    private static final String ERR_TIME_OUT = "Időtúllépés a lekérdezés közben! (" + CONNECTION_TIMEOUT + " sec)";
    
    public static NewDownloadForm getInstance(java.awt.Frame parent, boolean modal){
        if(instance == null){
            instance = new NewDownloadForm(parent, modal);
            instance.init();
        }
        else{
            instance.resetForm();
        }
        
        return instance;
    }
    
    private void init(){
        lblError.setVisible(false);
        cbModel = new DefaultComboBoxModel();
        
        cbQuality.setModel(cbModel);
        chbRemoveOriginalVideo.setEnabled(false);
        chbConvertToMp3.setEnabled(new File(Settings.getInstance().getFfmpegPath()).exists());
    }
    
    private void resetForm(){
        lblError.setVisible(false);
        lblTitle.setText("");
        tfVideoURL.setBackground(Color.WHITE);
        tfVideoURL.setText("");
        chbConvertToMp3.setSelected(false);
        chbRemoveOriginalVideo.setSelected(false);
        chbRemoveOriginalVideo.setEnabled(false);
        btnOK.setEnabled(false);
        
        try{
            cbModel.removeAllElements();
        }
        catch(ArrayIndexOutOfBoundsException ignore){}
    }
    
    private NewDownloadForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        init();
        
        ActionListener escListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        
        getRootPane().registerKeyboardAction(escListener, 
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), 
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        setIconImage(
            new ImageIcon(getClass().getResource("/Resources/youtube_downloader.png")).getImage()
        );
        
        setLocationRelativeTo(parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblVideoUrl = new javax.swing.JLabel();
        tfVideoURL = new javax.swing.JTextField();
        lblDownloadQuality = new javax.swing.JLabel();
        cbQuality = new javax.swing.JComboBox();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblError = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        chbConvertToMp3 = new javax.swing.JCheckBox();
        chbRemoveOriginalVideo = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Videó URL hozzáadása");

        lblVideoUrl.setText("Videó URL megadása a letöltéshez:");

        tfVideoURL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfVideoURLKeyReleased(evt);
            }
        });

        lblDownloadQuality.setText("Letöltés minősége:");

        cbQuality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbQualityActionPerformed(evt);
            }
        });

        btnOK.setText("OK");
        btnOK.setEnabled(false);
        btnOK.setMaximumSize(new java.awt.Dimension(63, 23));
        btnOK.setMinimumSize(new java.awt.Dimension(63, 23));
        btnOK.setPreferredSize(new java.awt.Dimension(63, 23));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Mégse");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblError.setForeground(new java.awt.Color(255, 0, 0));
        lblError.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/link_error.png"))); // NOI18N
        lblError.setText("Érvénytelen URL!");
        lblError.setToolTipText("Érvénytelen Youtube linket adott meg!");

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 130, 0));

        chbConvertToMp3.setText("Videó konvertálása MP3 formátumba");
        chbConvertToMp3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbConvertToMp3ActionPerformed(evt);
            }
        });

        chbRemoveOriginalVideo.setText("Eredeti videó törlése konvertálás után");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfVideoURL)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblDownloadQuality)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbQuality, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblError)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblVideoUrl)
                            .addComponent(lblTitle)
                            .addComponent(chbConvertToMp3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chbRemoveOriginalVideo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVideoUrl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfVideoURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbQuality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDownloadQuality))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbConvertToMp3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbRemoveOriginalVideo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblError))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        result = RESULT_CANCEL;
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        if(urls != null){
            url = urls.get(cbQuality.getSelectedIndex());
            
            result = RESULT_OK;
            setVisible(false);
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void tfVideoURLKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfVideoURLKeyReleased
        if(!tfVideoURL.getText().isEmpty() &&   YoutubeVideoInfo.isValidURL(tfVideoURL.getText())){
            tfVideoURL.setBackground(Color.WHITE);
            lblError.setVisible(false);
            
            updateQualities();
        }
        else{
            cbModel.removeAllElements();
            lblTitle.setText("");
            
            lblError.setText(ERR_INVALID_URL);
            lblError.setForeground(new java.awt.Color(255, 0, 0));
            
            tfVideoURL.setBackground(Color.PINK);
            lblError.setVisible(true);
        }
    }//GEN-LAST:event_tfVideoURLKeyReleased

    private void cbQualityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbQualityActionPerformed
        if(MainForm.getInstance().urlExists(urls.get(cbQuality.getSelectedIndex()))){
            tfVideoURL.setBackground(new Color(255,255,128));
            lblError.setVisible(true);
            btnOK.setEnabled(false);

            lblError.setText(ERR_ALREADY_EXISTS);
            lblError.setForeground(new java.awt.Color(187, 136, 0));
        }
        else{
            tfVideoURL.setBackground(Color.WHITE);
            lblError.setVisible(false);
            if(urls.size() > 0){
                btnOK.setEnabled(true);
            }
            else{
                btnOK.setEnabled(false);
            }
        }
    }//GEN-LAST:event_cbQualityActionPerformed

    private void chbConvertToMp3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbConvertToMp3ActionPerformed
        chbRemoveOriginalVideo.setEnabled(chbConvertToMp3.isSelected());
        chbRemoveOriginalVideo.setSelected(chbConvertToMp3.isSelected());
    }//GEN-LAST:event_chbConvertToMp3ActionPerformed

    private void setQualities(ActionEvent e) {
        YoutubeVideoInfo yvi = new YoutubeVideoInfo(tfVideoURL.getText());
        try {
            cbModel.removeAllElements();
            lblTitle.setText("");
            
            urls = yvi.getURLs();
            
            for(YoutubeURL url : urls){
                cbModel.addElement(url.videoformat.desc);
                
                if(lblTitle.getText().isEmpty()){
                    lblTitle.setText(url.title);
                }
            }
            
            cbQuality.setSelectedIndex(cbModel.getSize()-1);
            
            if(MainForm.getInstance().urlExists(urls.get(cbQuality.getSelectedIndex()))
            ){
                tfVideoURL.setBackground(new Color(255,255,128));
                lblError.setVisible(true);

                lblError.setText("Ez a videó már a letöltési listában van!");
                lblError.setForeground(new java.awt.Color(187, 136, 0));
            }
            else{
                tfVideoURL.setBackground(Color.WHITE);
                lblError.setVisible(false);
                if(urls.size() > 0){
                    btnOK.setEnabled(true);
                }
                else{
                    btnOK.setEnabled(false);
                }
            }
        } 
        catch (MalformedURLException ex){
            JOptionPane.showMessageDialog(NewDownloadForm.this, "Érvénytelen URL adott meg!", "Videó URL hozzáadó!", JOptionPane.WARNING_MESSAGE);
        }
        catch(SocketTimeoutException ex){
            lblError.setText(ERR_TIME_OUT);
            lblError.setVisible(true);
        }
        catch (IOException ex){
            if(ex.getMessage().equals("connect timed out")){
                lblError.setText(ERR_TIME_OUT);
                lblError.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(NewDownloadForm.this, "Adatátviteli hiba!\n"+ex.getMessage(), "Videó URL hozzáadó!", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (YoutubeInvalidContentException ex){
            JOptionPane.showMessageDialog(NewDownloadForm.this, "Hibás videó információt kaptunk a youtube-től!\n" + ex.getMessage(), "Videó URL hozzáadó!", JOptionPane.ERROR_MESSAGE);
        }
        catch (YoutubeErrorStatusException ex){
            JOptionPane.showMessageDialog(NewDownloadForm.this, "A videó nem tölthető le!\nIndok: " + ex.getMessage(), "Videó URL hozzáadó!", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox cbQuality;
    private javax.swing.JCheckBox chbConvertToMp3;
    private javax.swing.JCheckBox chbRemoveOriginalVideo;
    private javax.swing.JLabel lblDownloadQuality;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVideoUrl;
    private javax.swing.JTextField tfVideoURL;
    // End of variables declaration//GEN-END:variables

    private void updateQualities() {
        qualityDetectAborted = false;
        
        if(timer == null){
            timer = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timer.stop();
                    setQualities(e);
                }
            });
        }
        else if(timer.isRunning()){
            timer.stop();
        }
        
        timer.start();
        
        if(timeoutTimer == null){
            timeoutTimer = new Timer(5000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    qualityDetectAborted = true;
                }
            });
        }
        else if(timeoutTimer.isRunning()){
            timeoutTimer.stop();
        }
    }
    
    public int Show(){
        setVisible(true);
        return result;
    }
    
    public YoutubeURL getURL(){
        url.setConvertToMp3(chbConvertToMp3.isSelected());
        url.setRemoveOriginal(chbRemoveOriginalVideo.isSelected());
        return url;
    }
}
