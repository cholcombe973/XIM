package xim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class AimMsgWindow extends JFrame {
  private JPanel contentPane;
  private JButton jButtonSend = new JButton();
  private JTextPane jTextPane1 = new JTextPane();
  private JTextPane jTextPane2 = new JTextPane();
  private JScrollPane jScrollPane1 = new JScrollPane(jTextPane2);
  private JScrollPane jScrollPane2 = new JScrollPane(jTextPane1);
  private File send = new File("C:\\Documents and Settings\\Chris Holcombe\\My Documents\\imsend.mp3");
  private Hashtable actions;
  private JMenuBar jMenuBar1 = new JMenuBar();
  private JMenu jMenuFile = new JMenu();
  private JMenuItem jMenuItemAway = new JMenuItem();
  private JMenuItem jMenuItemExit = new JMenuItem();
  private JMenu jMenuEdit = new JMenu();
  private JMenu jMenuStyle = new JMenu();
  private String userName = new String();
  private Manager m;
  private JOptionPane jOptionPane1 = new JOptionPane();
  private AimBot aim;
  public boolean aimMsgWindowSoundPlaying = false;

  public AimMsgWindow(String user,Manager m,AimBot aim) {
    this.userName = user;
    this.m = m;
    this.aim = aim;
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception  {
    createActionTable(jTextPane1);
    contentPane = (JPanel) this.getContentPane();
    jButtonSend.setBounds(new Rectangle(264, 307, 82, 33));
    jButtonSend.setText("Send");
    jButtonSend.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonSend_actionPerformed();
      }
    });
    this.setTitle("Aim Msg Window from: " + userName);
    this.setSize(new Dimension(395, 400));
    contentPane.setLayout(null);
    jScrollPane1.setBounds(new Rectangle(26, 17, 351, 132));
    jScrollPane2.setBounds(new Rectangle(27, 169, 346, 126));
    jMenuFile.setText("File");
    jMenuItemAway.setText("Set Away");
    jMenuItemExit.setText("Exit");
    jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItemExit_actionPerformed(e);
      }
    });
    jMenuItemAway.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItemAway_actionPerformed(e);
      }
    });
    jMenuEdit.setText("Edit");
    jMenuStyle.setText("Format");
    contentPane.add(jButtonSend, null);
    contentPane.add(jScrollPane2, null);
    contentPane.add(jScrollPane1, null);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuEdit);
    jMenuBar1.add(jMenuStyle);
    jMenuFile.add(jMenuItemAway);
    jMenuFile.addSeparator();
    jMenuFile.add(jMenuItemExit);
    jTextPane1.addKeyListener(new KeyAdapter(this));
    jTextPane2.setEditable(false);
    jMenuEdit.addSeparator();
    jMenuEdit.add(getActionByName(DefaultEditorKit.cutAction));
    jMenuEdit.add(getActionByName(DefaultEditorKit.copyAction));
    jMenuEdit.add(getActionByName(DefaultEditorKit.pasteAction));
    jMenuEdit.add(getActionByName(DefaultEditorKit.selectAllAction));
    Action boldAction = new StyledEditorKit.BoldAction();
    boldAction.putValue(Action.NAME,"Bold");
    jMenuStyle.add(boldAction);
    Action italicAction = new StyledEditorKit.ItalicAction();
    italicAction.putValue(Action.NAME,"Italic");
    jMenuStyle.add(italicAction);
    Action underlineAction = new StyledEditorKit.UnderlineAction();
    underlineAction.putValue(Action.NAME,"Underline");
    jMenuStyle.add(underlineAction);
    //font actions
    Action fontAction = new StyledEditorKit.FontSizeAction("Font 10",10);
    jMenuStyle.add(fontAction);
    Action fontAction1 = new StyledEditorKit.FontSizeAction("Font 12",12);
    jMenuStyle.add(fontAction1);
    Action fontAction2 = new StyledEditorKit.FontSizeAction("Font 14",14);
    jMenuStyle.add(fontAction2);
    Action fontAction3 = new StyledEditorKit.FontSizeAction("Font 16",16);
    jMenuStyle.add(fontAction3);
    Action fontAction4 = new StyledEditorKit.FontSizeAction("Font 18",18);
    jMenuStyle.add(fontAction4);
    //end font actions
    this.setJMenuBar(jMenuBar1);

  }
  private Action getActionByName(String name){
    return (Action)(actions.get(name));
  }
  private void createActionTable(JTextComponent textComponent){
    actions = new Hashtable();
    Action[] actionsArray = textComponent.getActions();
    for(int i=0;i<actionsArray.length;i++){
      Action a = actionsArray[i];
      actions.put(a.getValue(Action.NAME),a);
    }
  }
  private void jMenuItemExit_actionPerformed(ActionEvent e) {
    this.dispose(); //save some resources
  }
  private void jMenuItemAway_actionPerformed(ActionEvent e){
    aim.setAway(true);
  }
  private void fillTextPane(String s){
    jTextPane2.setText(jTextPane2.getText()+"\n"+s);
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
        jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
      }
    });
  }
  public void jButtonSend_actionPerformed() {
      if(jTextPane1.getText().length()>0){
        fillTextPane("To " + userName + ": " +  jTextPane1.getText());
        aim.sendIM(userName,jTextPane1.getText());
      }
      jTextPane1.setText("");
      if(!aimMsgWindowSoundPlaying){
        JavaZoom zoom = new JavaZoom(this);
        zoom.play(send.toString());
        zoom.start();
      }
      setScrollBar();
}
  private void setScrollBar(){
    jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
  }
  public void update(String s){
    fillTextPane(s);
  }
}