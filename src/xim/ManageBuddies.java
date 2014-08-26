package xim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ManageBuddies extends JFrame{
  private XimFrame f;
  private JPanel contentPane;
  private JButton jButtonAddBuddy = new JButton();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JTextField jTextFieldUser = new JTextField();
  private JTextField jTextFieldIP = new JTextField(2);
  private Manager m;
  private JOptionPane jOptionPane = new JOptionPane();
  private JTextField jTextFieldIP1 = new JTextField();
  private JTextField jTextFieldIP2 = new JTextField();
  private JTextField jTextFieldIP3 = new JTextField();
  private JLabel jLabel3 = new JLabel();
  private JLabel jLabel4 = new JLabel();
  private JLabel jLabel5 = new JLabel();

  //Construct the frame
  public ManageBuddies() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {
    contentPane = (JPanel) this.getContentPane();
    jButtonAddBuddy.setBounds(new Rectangle(237, 160, 100, 27));
    jButtonAddBuddy.setText("Add Buddy");
    jButtonAddBuddy.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonAddBuddy_actionPerformed(e);
      }
    });
    this.setTitle("Buddy Manager");
    this.setSize(new Dimension(400, 229));
    contentPane.setLayout(null);
    jLabel1.setText("UserName:");
    jLabel1.setBounds(new Rectangle(43, 33, 97, 29));
    jLabel2.setText("User IP Address:");
    jLabel2.setBounds(new Rectangle(43, 69, 97, 21));
    jTextFieldUser.setBounds(new Rectangle(209, 38, 168, 22));
    jTextFieldIP.setBounds(new Rectangle(209, 69, 34, 20));
    //Register listeners for keeping the textField size 3 char's
    jTextFieldIP.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        textValueChanged(jTextFieldIP);
      }
    });
    jTextFieldIP1.setBounds(new Rectangle(254, 69, 34, 20));
    jTextFieldIP1.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        textValueChanged(jTextFieldIP1);
      }
    });
    jTextFieldIP2.setBounds(new Rectangle(299, 69, 34, 20));
    jTextFieldIP2.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        textValueChanged(jTextFieldIP2);
      }
    });
    jTextFieldIP3.setBounds(new Rectangle(344, 69, 34, 20));
    jTextFieldIP3.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        textValueChanged(jTextFieldIP3);
      }
    });
    jLabel3.setBounds(new Rectangle(245, 73, 3, 17));
    jLabel3.setText(".");
    jLabel4.setBounds(new Rectangle(289, 73, 3, 17));
    jLabel4.setText(".");
    jLabel5.setBounds(new Rectangle(334, 73, 3, 17));
    jLabel5.setText(".");
    contentPane.add(jLabel1, null);
    contentPane.add(jLabel2, null);
    contentPane.add(jButtonAddBuddy, null);
    contentPane.add(jTextFieldUser, null);
    contentPane.add(jTextFieldIP, null);
    contentPane.add(jTextFieldIP1, null);
    contentPane.add(jTextFieldIP2, null);
    contentPane.add(jLabel3, null);
    contentPane.add(jLabel4, null);
    contentPane.add(jLabel5, null);
    contentPane.add(jTextFieldIP3, null);
  }
  private void jButtonAddBuddy_actionPerformed(ActionEvent e) {
    //this method should check to see if the ip address if valid
    // handle all the collection of data here and setup buddy list.
    String ip = jTextFieldIP.getText()+"."+jTextFieldIP1.getText()+"."+jTextFieldIP2.getText()+"."+jTextFieldIP3.getText();
    String userName = jTextFieldUser.getText();
    if(!m.addUser(userName,ip)){
      JOptionPane.showMessageDialog(jOptionPane,"User already added");
    }
    jTextFieldIP.setText("");
    jTextFieldUser.setText("");

  }
  public void passReference(XimFrame f){
    this.f = f;
  }
  public void passManagerReference(Manager m){
    this.m = m;
  }
  private void textValueChanged(JTextField t) {
   // t is the target textField
   String  s = t.getText();
   int col = t.getColumns(); // get maximum textfield length
   /* check string length against maximum */
   if ((0 < col) && (col < s.length())) {
     int cp = t.getCaretPosition(); // get caret position after change
     t.setText(s = s.substring(0, col)); // adjust string to maximum size
     t.setCaretPosition(cp > col-1 ? col-1 : cp); // caret never trespass size limits
   }
  }
}