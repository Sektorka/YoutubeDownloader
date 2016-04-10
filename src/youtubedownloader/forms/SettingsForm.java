package youtubedownloader.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;
import javax.xml.parsers.ParserConfigurationException;
import youtubedownloader.classes.Settings;
import youtubedownloader.classes.YoutubeSettingsException;

public class SettingsForm extends javax.swing.JDialog {
    private Settings s;
    private final DefaultComboBoxModel cbThemeModel;
    
    public SettingsForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        
        cbThemeModel = new javax.swing.DefaultComboBoxModel();
        
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            cbThemeModel.addElement(info.getName());

        }
        
        initComponents();
        
        settingsTabs.remove(panelDb);
        
        setLocationRelativeTo(this);
        s = Settings.getInstance();
        
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
        
        JFormattedTextField txt = ((JSpinner.NumberEditor) spinDbPort.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
        txt.setHorizontalAlignment(JTextField.LEFT);
        
        txt = ((JSpinner.NumberEditor) spinProxyPort.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
        txt.setHorizontalAlignment(JTextField.LEFT);
        
        chbAutoDownload.setSelected(s.isbAutoDownload());
        chbDeleteUnfinisedVideoFile.setSelected(s.isbDeleteUnfinisedVideoFile());
        tfDownloadPath.setText(s.getStrDownloadPath());
        tfReplaceChar.setText(s.getReplaceKeyWord());
        cbDefaultOperation.setSelectedIndex(s.getDefualtOperation());
        tfDbHost.setText(s.getDbHost());
        spinDbPort.setValue(s.getDbPort());
        tfDbUser.setText(s.getDbUser());
        tfDbPassword.setText(s.getDbPass());
        tfDbName.setText(s.getDbName());
        tfProxyHost.setText(s.getProxyHost());
        spinProxyPort.setValue(s.getProxyPort());
        tfProxyUser.setText(s.getProxyUser());
        tfProxyPassword.setText(s.getProxyPass());
        tfFFMpegPath.setText(s.getFfmpegPath());
        cbAppTheme.setSelectedItem(s.getAppTheme());
        chbUseProxy.setSelected(s.isUseProxy());
        
        enableProxyFields(s.isUseProxy());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dirSelector = new javax.swing.JFileChooser();
        ffmpegSelector = new javax.swing.JFileChooser();
        settingsTabs = new javax.swing.JTabbedPane();
        panelGeneral = new javax.swing.JPanel();
        chbAutoDownload = new javax.swing.JCheckBox();
        chbDeleteUnfinisedVideoFile = new javax.swing.JCheckBox();
        lblDownloadPath = new javax.swing.JLabel();
        tfDownloadPath = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        lblSpecChars = new javax.swing.JLabel();
        tfReplaceChar = new javax.swing.JTextField();
        lblVideoDoubleClick = new javax.swing.JLabel();
        cbDefaultOperation = new javax.swing.JComboBox();
        lblFFMpegPath = new javax.swing.JLabel();
        tfFFMpegPath = new javax.swing.JTextField();
        btnBrowseFFMpeg = new javax.swing.JButton();
        lblAppTheme = new javax.swing.JLabel();
        cbAppTheme = new javax.swing.JComboBox();
        panelDb = new javax.swing.JPanel();
        lblDbHost = new javax.swing.JLabel();
        lblDbPort = new javax.swing.JLabel();
        lblDbUser = new javax.swing.JLabel();
        lblDbPassword = new javax.swing.JLabel();
        lblDbName = new javax.swing.JLabel();
        tfDbUser = new javax.swing.JTextField();
        tfDbHost = new javax.swing.JTextField();
        tfDbName = new javax.swing.JTextField();
        tfDbPassword = new javax.swing.JPasswordField();
        spinDbPort = new javax.swing.JSpinner();
        panelProxy = new javax.swing.JPanel();
        lblDbHost1 = new javax.swing.JLabel();
        tfProxyHost = new javax.swing.JTextField();
        lblDbPort1 = new javax.swing.JLabel();
        spinProxyPort = new javax.swing.JSpinner();
        lblDbUser1 = new javax.swing.JLabel();
        tfProxyUser = new javax.swing.JTextField();
        lblDbPassword1 = new javax.swing.JLabel();
        tfProxyPassword = new javax.swing.JPasswordField();
        chbUseProxy = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();

        dirSelector.setApproveButtonText("Megnyit");
        dirSelector.setApproveButtonToolTipText("Mappa kiválasztása");
        dirSelector.setDialogTitle("Videó helyének megadása");
        dirSelector.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        dirSelector.setToolTipText("");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Beállítások");
        setResizable(false);

        chbAutoDownload.setText("Videó automatikus letöltése");

        chbDeleteUnfinisedVideoFile.setText("Befejezetlen letöltés eltávolításakor a fájlt is távolítsuk el");

        lblDownloadPath.setText("Letöltések helye:");

        tfDownloadPath.setEditable(false);

        btnBrowse.setText("...");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        lblSpecChars.setText("Speciális karakterek \\/?:*\"><| cserélése a fájlnében, a következő karakterre:");

        tfReplaceChar.setText("_");

        lblVideoDoubleClick.setText("Videón duplán kattintva:");

        cbDefaultOperation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Videó lejátszása", "Videót tartalmazó mappa megnyitása", "Videó megtekintése youtube-on" }));

        lblFFMpegPath.setText("FFMpeg helye:");

        tfFFMpegPath.setEditable(false);

        btnBrowseFFMpeg.setText("...");
        btnBrowseFFMpeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseFFMpegActionPerformed(evt);
            }
        });

        lblAppTheme.setText("Stílus:");

        cbAppTheme.setModel(cbThemeModel);

        javax.swing.GroupLayout panelGeneralLayout = new javax.swing.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addComponent(lblSpecChars)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfReplaceChar, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addComponent(lblVideoDoubleClick)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbDefaultOperation, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chbDeleteUnfinisedVideoFile)
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addComponent(lblDownloadPath)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfDownloadPath)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowse))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGeneralLayout.createSequentialGroup()
                        .addComponent(lblFFMpegPath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfFFMpegPath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowseFFMpeg))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addComponent(chbAutoDownload)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addComponent(lblAppTheme)
                        .addGap(128, 128, 128)
                        .addComponent(cbAppTheme, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chbAutoDownload)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbDeleteUnfinisedVideoFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDownloadPath)
                    .addComponent(tfDownloadPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFFMpegPath)
                    .addComponent(tfFFMpegPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowseFFMpeg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfReplaceChar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSpecChars))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVideoDoubleClick)
                    .addComponent(cbDefaultOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAppTheme)
                    .addComponent(cbAppTheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        settingsTabs.addTab("Általános", new javax.swing.ImageIcon(getClass().getResource("/Resources/cog_edit.png")), panelGeneral); // NOI18N

        lblDbHost.setText("Kiszolgáló:");

        lblDbPort.setText("Port:");

        lblDbUser.setText("Felhasználónév:");

        lblDbPassword.setText("Jelszó:");

        lblDbName.setText("Adatbázisnév:");

        spinDbPort.setModel(new javax.swing.SpinnerNumberModel(3306, 1, 65535, 1));
        spinDbPort.setEditor(new javax.swing.JSpinner.NumberEditor(spinDbPort, ""));

        javax.swing.GroupLayout panelDbLayout = new javax.swing.GroupLayout(panelDb);
        panelDb.setLayout(panelDbLayout);
        panelDbLayout.setHorizontalGroup(
            panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDbUser)
                    .addComponent(lblDbPassword)
                    .addComponent(lblDbPort)
                    .addComponent(lblDbHost)
                    .addComponent(lblDbName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfDbHost, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addComponent(tfDbUser)
                    .addComponent(tfDbName)
                    .addComponent(tfDbPassword)
                    .addComponent(spinDbPort))
                .addContainerGap())
        );
        panelDbLayout.setVerticalGroup(
            panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbHost)
                    .addComponent(tfDbHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbPort)
                    .addComponent(spinDbPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbUser)
                    .addComponent(tfDbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbPassword)
                    .addComponent(tfDbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbName)
                    .addComponent(tfDbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabs.addTab("Adatbázis", new javax.swing.ImageIcon(getClass().getResource("/Resources/database_edit.png")), panelDb); // NOI18N

        lblDbHost1.setText("Kiszolgáló:");

        tfProxyHost.setEnabled(false);

        lblDbPort1.setText("Port:");

        spinProxyPort.setModel(new javax.swing.SpinnerNumberModel(3128, 1, 65535, 1));
        spinProxyPort.setEditor(new javax.swing.JSpinner.NumberEditor(spinProxyPort, ""));
        spinProxyPort.setEnabled(false);

        lblDbUser1.setText("Felhasználónév:");

        tfProxyUser.setEnabled(false);

        lblDbPassword1.setText("Jelszó:");

        tfProxyPassword.setEnabled(false);

        chbUseProxy.setText("Proxy használata");
        chbUseProxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbUseProxyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelProxyLayout = new javax.swing.GroupLayout(panelProxy);
        panelProxy.setLayout(panelProxyLayout);
        panelProxyLayout.setHorizontalGroup(
            panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProxyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProxyLayout.createSequentialGroup()
                        .addGroup(panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDbUser1)
                            .addComponent(lblDbPassword1)
                            .addComponent(lblDbPort1)
                            .addComponent(lblDbHost1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfProxyHost, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                            .addComponent(tfProxyUser)
                            .addComponent(tfProxyPassword)
                            .addComponent(spinProxyPort)))
                    .addGroup(panelProxyLayout.createSequentialGroup()
                        .addComponent(chbUseProxy)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelProxyLayout.setVerticalGroup(
            panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProxyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chbUseProxy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbHost1)
                    .addComponent(tfProxyHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbPort1)
                    .addComponent(spinProxyPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbUser1)
                    .addComponent(tfProxyUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDbPassword1)
                    .addComponent(tfProxyPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(129, Short.MAX_VALUE))
        );

        settingsTabs.addTab("Proxy", new javax.swing.ImageIcon(getClass().getResource("/Resources/proxy.png")), panelProxy); // NOI18N

        btnCancel.setText("Mégse");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(settingsTabs)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(settingsTabs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        settingsTabs.getAccessibleContext().setAccessibleName("tabGeneral");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        setDirPath();
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        try{
            saveSettings();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(MainForm.getInstance(), e.getMessage(),"Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnBrowseFFMpegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseFFMpegActionPerformed
        setFFMpegPath();
    }//GEN-LAST:event_btnBrowseFFMpegActionPerformed

    private void chbUseProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbUseProxyActionPerformed
        enableProxyFields(chbUseProxy.isSelected());
    }//GEN-LAST:event_chbUseProxyActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnBrowseFFMpeg;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox cbAppTheme;
    private javax.swing.JComboBox cbDefaultOperation;
    private javax.swing.JCheckBox chbAutoDownload;
    private javax.swing.JCheckBox chbDeleteUnfinisedVideoFile;
    private javax.swing.JCheckBox chbUseProxy;
    private javax.swing.JFileChooser dirSelector;
    private javax.swing.JFileChooser ffmpegSelector;
    private javax.swing.JLabel lblAppTheme;
    private javax.swing.JLabel lblDbHost;
    private javax.swing.JLabel lblDbHost1;
    private javax.swing.JLabel lblDbName;
    private javax.swing.JLabel lblDbPassword;
    private javax.swing.JLabel lblDbPassword1;
    private javax.swing.JLabel lblDbPort;
    private javax.swing.JLabel lblDbPort1;
    private javax.swing.JLabel lblDbUser;
    private javax.swing.JLabel lblDbUser1;
    private javax.swing.JLabel lblDownloadPath;
    private javax.swing.JLabel lblFFMpegPath;
    private javax.swing.JLabel lblSpecChars;
    private javax.swing.JLabel lblVideoDoubleClick;
    private javax.swing.JPanel panelDb;
    private javax.swing.JPanel panelGeneral;
    private javax.swing.JPanel panelProxy;
    private javax.swing.JTabbedPane settingsTabs;
    private javax.swing.JSpinner spinDbPort;
    private javax.swing.JSpinner spinProxyPort;
    private javax.swing.JTextField tfDbHost;
    private javax.swing.JTextField tfDbName;
    private javax.swing.JPasswordField tfDbPassword;
    private javax.swing.JTextField tfDbUser;
    private javax.swing.JTextField tfDownloadPath;
    private javax.swing.JTextField tfFFMpegPath;
    private javax.swing.JTextField tfProxyHost;
    private javax.swing.JPasswordField tfProxyPassword;
    private javax.swing.JTextField tfProxyUser;
    private javax.swing.JTextField tfReplaceChar;
    // End of variables declaration//GEN-END:variables

    private void enableProxyFields(boolean enable){
        tfProxyHost.setEnabled(enable);
        tfProxyUser.setEnabled(enable);
        tfProxyPassword.setEnabled(enable);
        spinProxyPort.setEnabled(enable);
    }
    
    private void setDirPath() {
        if(dirSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            tfDownloadPath.setText(dirSelector.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void setFFMpegPath() {
        if(ffmpegSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            tfFFMpegPath.setText(ffmpegSelector.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveSettings() throws YoutubeSettingsException, ParserConfigurationException, 
            IllegalArgumentException, IllegalAccessException, IOException {
        
        s.setbAutoDownload(chbAutoDownload.isSelected());
        s.setbDeleteUnfinisedVideoFile(chbDeleteUnfinisedVideoFile.isSelected());
        s.setStrDownloadPath(tfDownloadPath.getText());
        s.setReplaceKeyWord(tfReplaceChar.getText());
        s.setDefualtOperation(cbDefaultOperation.getSelectedIndex());
        s.setDbHost(tfDbHost.getText());
        s.setDbPort(Integer.parseInt(spinDbPort.getValue().toString()));
        s.setDbUser(tfDbUser.getText());
        s.setDbPass(new String(tfDbPassword.getPassword()));
        s.setDbName(tfDbName.getText());
        s.setProxyHost(tfProxyHost.getText());
        s.setProxyPort(Integer.parseInt(spinProxyPort.getValue().toString()));
        s.setProxyUser(tfProxyUser.getText());
        s.setProxyPass(new String(tfProxyPassword.getPassword()));
        s.setFfmpegPath(tfFFMpegPath.getText());
        s.setAppTheme(cbAppTheme.getSelectedItem().toString());
        s.setUseProxy(chbUseProxy.isSelected());
        
        s.saveSettings();
        dispose();
    }
}
