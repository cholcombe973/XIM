package xim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class XimFrame extends JFrame {
  private JPanel contentPane;
  private JMenuBar jMenuBar1 = new JMenuBar();
  private JMenu jMenuFile = new JMenu();
  private JMenu jMenuAim = new JMenu();
  private JMenuItem jMenuAimInfo = new JMenuItem();
  private JMenuItem jMenuFileExit = new JMenuItem();
  private JMenuItem jMenuFileMsg = new JMenuItem();
  private JMenuItem jMenuFileOptions = new JMenuItem();
  private JMenu jMenuHelp = new JMenu();
  private JMenuItem jMenuHelpAbout = new JMenuItem();
  private JButton jButtonMsg = new JButton();
  private JList jListBuddies;
  private JButton jButtonAddBuddy = new JButton();
  private JScrollPane jScrollPane1;
  private DefaultListModel listModel;
  private JOptionPane jOptionPane1 = new JOptionPane();
  private CoreReceive cr;
  private MsgWindow w;
  private ManageBuddies mb;
  private Manager m;
  private String userName = new String();
  private JButton jButtonPrivate = new JButton();
  private boolean msgWindowOpen = false;
  private JButton jButtonDirectConnect = new JButton();
  private JButton jButtonAim = new JButton();
  private AimBot aim;
  private ReferenceManager rm;

  //Construct the frame
  public XimFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public XimFrame(ReferenceManager rm) {
    this.rm = rm;
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }catch(Exception e) {e.printStackTrace();}
  }

  //Component initialization
  private void jbInit() throws Exception  {
    listModel = new DefaultListModel();
    jListBuddies = new JList(listModel);
    jListBuddies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jScrollPane1 = new JScrollPane(jListBuddies);
    //setIconImage(Toolkit.getDefaultToolkit().createImage(XimFrame.class.getResource("[Your Icon]")));
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(null);
    this.setSize(new Dimension(292, 490));
    this.setTitle("XIM");
    jMenuFile.setActionMap(null);
    jMenuFile.setText("File");
    jMenuAim.setText("Aim");
    jMenuAimInfo.setText("User Info");
    jMenuAimInfo.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if(jListBuddies.getSelectedValue()!=null){
          String user = jListBuddies.getSelectedValue().toString();
          getInfo(user);
        }
      }
    });
    jMenuFileExit.setText("Exit");
    jMenuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.ALT_MASK));
    jMenuFileExit.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        jMenuFileExit_actionPerformed(e);
      }
    });
    jMenuFileMsg.setText("Msg Aim User");
    jMenuFileMsg.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        jMenuFileMsg_actionPerformed(e);
      }
    });
    jMenuFileOptions.setText("Setup");
    jMenuFileOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.ALT_MASK));
    jMenuFileOptions.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        jMenuFileOptions_actionPerformed(e);
      }
    });
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.ALT_MASK));
    jMenuHelpAbout.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        jMenuHelpAbout_actionPerformed(e);
      }
    });
    jButtonMsg.setBounds(new Rectangle(9, 367, 119, 20));
    jButtonMsg.setText("Send Message");
    jButtonMsg.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonMsg_actionPerformed(e);
      }
    });
    jButtonAddBuddy.setBounds(new Rectangle(9, 335, 119, 22));
    jButtonAddBuddy.setToolTipText("Manually add buddy");
    jButtonAddBuddy.setText("Add Buddy");
    jButtonAddBuddy.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonAddBuddy_actionPerformed(e);
      }
    });
    jScrollPane1.setBounds(new Rectangle(173, 10, 86, 413));
    jButtonPrivate.setBounds(new Rectangle(9, 400, 158, 23));
    jButtonPrivate.setToolTipText("Select a user from the list and click this button");
    jButtonPrivate.setText("Send Private Message");
    jButtonPrivate.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
       jButtonPrivate_actionPerformed();
      }
    });
    jButtonDirectConnect.setBounds(new Rectangle(9, 303, 119, 21));
    jButtonDirectConnect.setText("Direct Connect");
    jButtonDirectConnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonDirectConnect_actionPerformed(e);
      }
    });
    jButtonAim.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonAim_actionPerformed(e);
      }
    });
    jButtonAim.setText("Connect to AIM");
    jButtonAim.setBounds(new Rectangle(10, 271, 119, 20));
    jMenuFile.add(jMenuFileOptions);
    jMenuFile.add(jMenuFileMsg);
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    contentPane.add(jButtonMsg, null);
    contentPane.add(jButtonAddBuddy, null);
    contentPane.add(jScrollPane1, null);
    contentPane.add(jButtonPrivate, null);
    contentPane.add(jButtonDirectConnect, null);
    contentPane.add(jButtonAim, null);
    this.setJMenuBar(jMenuBar1);
  }
  public void displayUserDialog(){
    userName = "[".concat(JOptionPane.showInputDialog(jOptionPane1,"Enter Username: ","User Setup",JOptionPane.QUESTION_MESSAGE)).concat("]");
    cr.setUser(userName);
    try{
      String inet = InetAddress.getLocalHost().toString();
      String localAddress = inet.substring(inet.indexOf("/")+1,inet.length());
      m.addUser(userName.substring(userName.indexOf("[")+1,userName.indexOf("]")),localAddress);
      }catch(UnknownHostException e){System.out.println("Unknown host at DisplayUserDialog: " + e);}
  }

  //File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    cr.sendKillMessage();
    if(aim!=null){
      aim.shutdownAimBot();
    }
    System.exit(0);
  }
  //Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    XimFrame_AboutBox dlg = new XimFrame_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.setVisible(true);
  }
  public void jMenuFileOptions_actionPerformed(ActionEvent e){
    //Make new window to handle options settings.
    OptionsFrame of = new OptionsFrame();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = of.getSize();
    if(frameSize.height > screenSize.height){
      frameSize.height = screenSize.height;
    }
    if(frameSize.width > screenSize.width){
      frameSize.width = screenSize.width;
    }
    of.setLocation(0,0);
    of.setVisible(true);
    of.setManager(m);
  }
  private void jMenuFileMsg_actionPerformed(ActionEvent e){
    //open up the aim msg window here if a user is selected
    if(aim!=null){
      if(jListBuddies.getSelectedValue()!=null){
        String user = jListBuddies.getSelectedValue().toString();
        AimMsgWindow aw = new AimMsgWindow(user,m,aim);
        aw.validate();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = aw.getSize();
        if (frameSize.height > screenSize.height) {
          frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
          frameSize.width = screenSize.width;
        }
        m.registerAimWindow(user,aw);
        aw.setLocation(0,0);
        aw.setVisible(true);
      }else
        JOptionPane.showMessageDialog(jOptionPane1,"Please Select a user from the list!","Selection Error",JOptionPane.ERROR_MESSAGE);
    }else{
      //display error msg
      JOptionPane.showMessageDialog(jOptionPane1,"AimBot not open yet error!");
    }

  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }
  private void jButtonMsg_actionPerformed(ActionEvent e) {
    if(msgWindowOpen){
      w.setVisible(true);
    }
    else{
      w = new MsgWindow(rm);
      w.validate();
      //Center the window
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = w.getSize();
      if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
      }
      w.setLocation(0,0);
      w.setVisible(true);
      w.setReceive(cr);
      cr.passMsgWindowReference(w);
      msgWindowOpen = true;
    }
  }
  public boolean isPublicWindowOpen(){
    if(msgWindowOpen){
      return true;
    }
    else{
      return false;
    }
  }
  public void setReceive(CoreReceive cr){
   this.cr = cr;
  }
  private void jButtonAddBuddy_actionPerformed(ActionEvent e) {
    ManageBuddies frame = new ManageBuddies();
    frame.validate();
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation(0,0);
    frame.setVisible(true);
    frame.passManagerReference(m);
    frame.passReference(this);
  }
  public void passReferences(Manager m){
    this.m = m;
  }
  public void addBuddy(String userName){
    listModel.addElement(userName); //add new user to buddy list
  }
  public void removeBuddy(String userName){
    listModel.removeElement(userName);
  }
  private void jButtonDirectConnect_actionPerformed(ActionEvent e) {
    if(jListBuddies.getSelectedValue()!=null){
      //should open a window to ask for options such as port.
      //maybe UDP a message to user about options selected then setup socket
      //the world fucking blows!
      String user = jListBuddies.getSelectedValue().toString();

      JFileChooser fc = new JFileChooser();
      int choice = fc.OPEN_DIALOG;
      if(choice == JFileChooser.APPROVE_OPTION){
        File file = fc.getSelectedFile();
        try{
          int port = Integer.parseInt(JOptionPane.showInputDialog(jOptionPane1,"Input Port for connection","Setup",JOptionPane.PLAIN_MESSAGE));
          TcpSend tcp = new TcpSend(user,m,port,file,cr);
          }catch(NumberFormatException d){JOptionPane.showMessageDialog(jOptionPane1,"Enter port as integer","Input Error",JOptionPane.ERROR_MESSAGE);}
      }
    }else{
      JOptionPane.showMessageDialog(jOptionPane1,"Please Select a user from the list!","Selection Error",JOptionPane.ERROR_MESSAGE);
    }
  }
  private void jButtonPrivate_actionPerformed() {
    //Create a new msg window that holds a username
    if(jListBuddies.getSelectedValue()!=null){
      String user = jListBuddies.getSelectedValue().toString();
      String ip = m.getIP(user);
      PrivateMsgWindow pw = new PrivateMsgWindow(ip,cr,m);
      pw.validate();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = pw.getSize();
      if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
      }
      m.registerWindow(user,pw);
      pw.setLocation(0,0);
      pw.setVisible(true);
    }else{
      JOptionPane.showMessageDialog(jOptionPane1,"Please Select a user from the list!","Selection Error",JOptionPane.ERROR_MESSAGE);
    }
  }
  public void openPrivateWindow(String ip){
    if(!m.isPrivateOpen(ip)){
      PrivateMsgWindow pw = new PrivateMsgWindow(ip,cr,m);
      pw.validate();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = pw.getSize();
      if(frameSize.height > screenSize.height){
        frameSize.height = screenSize.height;
      }
      if(frameSize.width > screenSize.width){
        frameSize.width = screenSize.width;
      }
      String user = m.getUser(ip);
      m.registerWindow(user,pw);
      pw.setLocation(0,0);
      pw.setVisible(true);
    }
    else{
      //Private message window is already opened
      String user = m.getUser(ip);
      PrivateMsgWindow p = m.getWindow(user);
      p.setVisible(true);
    }
  }
  public AimMsgWindow openAimWindow(String user){
    AimMsgWindow aw = new AimMsgWindow(user,m,aim);
    aw.validate();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = aw.getSize();
    if(frameSize.height > screenSize.height){
      frameSize.height = screenSize.height;
    }
    if(frameSize.width > screenSize.width){
      frameSize.width = screenSize.width;
    }
    m.registerAimWindow(user,aw);
    aw.setLocation(0,0);
    aw.setVisible(true);
    return aw;
  }
  public void openPublicWindow(){
   if(!msgWindowOpen){
     w = new MsgWindow(rm);
     w.validate();
     //Center the window
     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
     Dimension frameSize = w.getSize();
     if (frameSize.height > screenSize.height) {
       frameSize.height = screenSize.height;
     }
     if (frameSize.width > screenSize.width) {
       frameSize.width = screenSize.width;
     }
     w.setLocation(0,0);
     w.setVisible(true);
     w.setReceive(cr);
     cr.passMsgWindowReference(w);
     msgWindowOpen = true;
   }
  }
  private void jButtonAim_actionPerformed(ActionEvent e) {
    String password = askPassword();
    if(password!=null){
      aim = new AimBot(m,this);
      aim.setUser(userName);
      aim.setPassword(password);
      aim.start();
    }else{
      JOptionPane.showMessageDialog(jOptionPane1,"No Password was entered","Password error",JOptionPane.ERROR_MESSAGE);
    }
  }
  private String askPassword(){
    JPasswordField pf = new JPasswordField();
    pf.setEchoChar('*');
    Object[] message = new Object[]{"Enter Password",pf};
    int selection = JOptionPane.showOptionDialog(jOptionPane1,message,"Password",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE,
                                     null,null,null);
    if(selection==0){
      return String.valueOf(pf.getPassword());
    }
    else{
      return null;
    }
  }
  public void createJOptionPane(String msg){
    JOptionPane.showMessageDialog(jOptionPane1,msg);
  }
  public void getInfo(String user){
    if(aim!=null){
      aim.getInfo(user);
    }
  }
}
