package main;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;



@SuppressWarnings("serial")
public class Frame extends JFrame {

	public static final Image ICON = Toolkit.getDefaultToolkit().getImage("icon.png");
	public static final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	public JTextArea textArea = new JTextArea();
	public static final String APPLICATION_NAME = "Calculation";
	private PopupMenu trayMenu = new PopupMenu();
	private final TrayIcon trayIcon =
				new TrayIcon(ICON, APPLICATION_NAME, trayMenu);;

	private JScrollPane scroll;

	public Frame() {
		frameCreate();
		scroll = new JScrollPane(textArea);
		scroll.setPreferredSize(new Dimension(
				(int) (0.2 * Toolkit.getDefaultToolkit().getScreenSize().width),
	    		(int) (0.1 * Toolkit.getDefaultToolkit().getScreenSize().height)));
		add(scroll, BorderLayout.CENTER);
		pack();
		setOnCenter(this);
	}

	public void printText(String text, boolean update) {
		textArea.setText(textArea.getText() + "\n" + text);
		repaint();
		if (update) {
			textArea.setText(textArea.getText() + "\n");
			textArea.updateUI();
		}
		scroll.getVerticalScrollBar().setValue(textArea.getHeight() - 10);
	}

	private void frameCreate() {
		setTitle("Calculation");
		setResizable(false);
		setVisible(true);
	    setLayout(new BorderLayout());
	    //setIconImage(ICON);
	    //setTrayIcon();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent event) {
            	   exitDialog();
            }
             public void windowDeactivated(WindowEvent event) {}
             public void windowDeiconified(WindowEvent event) {}
             public void windowIconified(WindowEvent event) {}
             public void windowOpened(WindowEvent event) {}
             public void windowActivated(WindowEvent event) {}
             public void windowClosed(WindowEvent event) {}
        });
	}

	private void setTrayIcon() {

	    if (!SystemTray.isSupported())
	    	return;

        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setVisible(true);
            	setExtendedState(JFrame.NORMAL);
            }
        });
        trayMenu.add(openItem);

           MenuItem exitItem = new MenuItem("Exit");
           exitItem.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
            	   Frame.exitDialog();
               }
           });
           trayMenu.add(exitItem);



	    trayIcon.setImageAutoSize(true);

	    trayIcon.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent evt) {
	        	if (evt.getClickCount() == 2) {
                	SystemTray.getSystemTray().remove(trayIcon);
                	setVisible(true);
                	Frame.setOnCenter(Frame.this);
                	setState(JFrame.NORMAL);
	        	}
	        }
	    });

	    this.addWindowStateListener(new WindowStateListener() {
	    	public void windowStateChanged(WindowEvent event) {

	    		if(event.getNewState() == JFrame.ICONIFIED) {
                	try {
                    	SystemTray.getSystemTray().add(trayIcon);
                    	setVisible(false);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                }

                if(event.getNewState() == JFrame.NORMAL) {
                	SystemTray.getSystemTray().remove(trayIcon);
                	Frame.setOnCenter(Frame.this);
                	setVisible(true);
                }
            }
        });
	  }

	public void notify(String text) {
		trayIcon.displayMessage(APPLICATION_NAME, text,
                TrayIcon.MessageType.INFO);
	}

	private static void setOnCenter(Window frame) {
		frame.setLocation(
				(screenWidth - frame.getWidth()) / 2,
				(screenHeight - frame.getHeight()) / 2);
	}

	public static void exitDialog() {
	   	  Object[] options = { "OK", "Cancel" };
	   	  int choice = JOptionPane.showOptionDialog(null, "You really want to quit?", "Quit?",
	   	      JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
	   	     null, options, options[0]);

	   	  if (choice == JOptionPane.YES_OPTION)
	   	    System.exit(0);
		}

}
