package xim;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class OptionsFrame extends JFrame {
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JTextField jReceiveField = new JTextField();
  private JTextField jSendField = new JTextField();
  private JButton jButton1 = new JButton();
  private JButton jButton2 = new JButton();
  private JButton jButton3 = new JButton();
  private JOptionPane jOptionPane1 = new JOptionPane();
  private JFileChooser fc = new JFileChooser();
  private Manager m;
  private File imReceive;
  private File imSend;

  public OptionsFrame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jLabel1.setText("Msg receive sound");
    jLabel1.setBounds(new Rectangle(38, 115, 108, 22));
    this.getContentPane().setLayout(null);
    this.setTitle("User Options");
    jLabel2.setText("Msg send sound");
    jLabel2.setBounds(new Rectangle(38, 167, 103, 17));
    jReceiveField.setBounds(new Rectangle(195, 114, 162, 21));
    jSendField.setBounds(new Rectangle(195, 168, 162, 21));
    jButton1.setBounds(new Rectangle(266, 139, 92, 20));
    jButton1.setText("Open");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    jButton2.setText("Open");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton2_actionPerformed(e);
      }
    });
    jButton2.setBounds(new Rectangle(264, 195, 92, 20));
    jButton3.setBounds(new Rectangle(264, 280, 92, 20));
    jButton3.setText("Ok");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton3_actionPerformed(e);
      }
    });
    this.getContentPane().add(jLabel1, null);
    this.getContentPane().add(jReceiveField, null);
    this.getContentPane().add(jLabel2, null);
    this.getContentPane().add(jButton1, null);
    this.getContentPane().add(jButton2, null);
    this.getContentPane().add(jSendField, null);
    this.getContentPane().add(jButton3, null);
    this.setSize(400,350);
  }

  void jButton1_actionPerformed(ActionEvent e) {
    //Show the user a file Open dialog box
    XimFileFilter filter = new XimFileFilter();
    filter.addExtension("mp3");
    filter.setDescription("Sound Files");
    fc.setFileFilter(filter);
    int returnVal = fc.showOpenDialog(jOptionPane1);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      jReceiveField.setText(file.toString());
      imReceive = file;
    }
  }

  void jButton2_actionPerformed(ActionEvent e) {
    //Show the user a file open dialog box
    XimFileFilter filter = new XimFileFilter();
    filter.addExtension("mp3");
    filter.setDescription("Sound Files");
    fc.setFileFilter(filter);
    int returnVal = fc.showOpenDialog(jOptionPane1);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      imSend = file;
      jSendField.setText(file.toString());
    }
  }
  public void setManager(Manager m){
    this.m = m;
  }
  void jButton3_actionPerformed(ActionEvent e) {
    //save inputs and close window
    if(imReceive!=null){
      m.storeReceiveFile(imReceive);
    }
    if(imSend !=null){
      m.storeSendFile(imSend);
    }
    this.setVisible(false);
  }
}