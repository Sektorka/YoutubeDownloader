package youtubedownloader.forms;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import youtubedownloader.classes.Settings;

public class AboutForm extends javax.swing.JDialog {
    public AboutForm(java.awt.Frame parent, boolean modal){
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(this);
        
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
        
        lblVersion.setText(Settings.VERSION);
        try {
            lblContact.setText("<html><a href=\"mailto:" +  Settings.CONTACT + "?subject=" + URLEncoder.encode(Settings.APP_NAME, "UTF-8") +"\">" + Settings.CONTACT + "</a></html>");
        } catch (UnsupportedEncodingException ex) {
            lblContact.setText(Settings.CONTACT);
        }
        lblContact.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendMail(lblContact);
    }
    
    private void sendMail(JLabel contact) {
        contact.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().mail(new URI("mailto:" + Settings.CONTACT + "?subject=" + URLEncoder.encode(Settings.APP_NAME, "UTF-8") ));
                } catch (URISyntaxException ex) {
                    JOptionPane.showMessageDialog(AboutForm.this, ex, "Program névjegy", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex){
                    JOptionPane.showMessageDialog(AboutForm.this, ex, "Program névjegy", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblImage = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        lblVersion = new javax.swing.JLabel();
        lblContact = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Névjegy");
        setResizable(false);

        lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/youtube_downloader.png"))); // NOI18N

        lblTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTitle.setText("Youtube videó letöltő");

        btnClose.setText("Bezárás");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        lblVersion.setText("v1.0.0");

        lblContact.setText("krisztisektor@gmail.com");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addComponent(lblVersion)
                    .addComponent(lblContact))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblImage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(btnClose))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addGap(3, 3, 3)
                        .addComponent(lblVersion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblContact)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel lblContact;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVersion;
    // End of variables declaration//GEN-END:variables
}
