package count;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class JCount extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField countToTF;
	private JLabel countedTillLabel;
	private JButton startButton;
	private JButton stopButton;
	
	private WorkerThread wt;
	
	public JCount() {
		
		countToTF = new JTextField("1000000", 1);
		countToTF.setSize(50, 10);
		countedTillLabel = new JLabel("0");
		
		//wt = new WorkerThread(countedTillLabel, Integer.parseInt(countToTF.getText()));
		
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				wt = new WorkerThread(countedTillLabel, Integer.parseInt(countToTF.getText()));
				wt.start();
			}
		});
		
		stopButton = new JButton("Stop");
		
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (wt != null && wt.isAlive()) wt.interrupt();
			}
		});
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setSize(50, 50);
		
		add(countToTF);
		add(countedTillLabel);
		add(startButton);
		add(stopButton);
		add(Box.createRigidArea(new Dimension(0, 40)));
	}
	
	
	/**
	 * 
	 */

	private static void createAndShowGUI(){
		JCount count1 = new JCount();
		JCount count2 = new JCount();
		JCount count3 = new JCount();
		JCount count4 = new JCount();
		
		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		pane.add(count1);
		pane.add(count2);
		pane.add(count3);
		pane.add(count4);
		
		frame.setSize(60, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
