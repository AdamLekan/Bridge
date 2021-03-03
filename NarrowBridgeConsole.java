import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

enum BusDirection{
	EAST,
	WEST;
	
	@Override
	public String toString(){
		switch(this){
		case EAST: 
			return "W";
		case WEST: 
			return "Z";
		}
		return "";
	} 
}  

 
enum Limits{
	NO_LIMIT,
	BIDIRECTIONAL,
	UNIDIRECTIONAL,
	ONE_BUS;
	

	
	@Override
	public String toString(){
		switch(this){
		case NO_LIMIT: 
			return "Ruch bez ograniczeń";
		case BIDIRECTIONAL: 
			return "Ruch dwukierunkowy(max 3 busy)";
		case UNIDIRECTIONAL: 
			return "Ruch jednokierunkowy(max 3 busy)";
		case ONE_BUS: 
			return "Ruch ograniczony(max 1 bus)";
		} 
		return "";
	}
} 


public class NarrowBridgeConsole extends JFrame implements ActionListener, ChangeListener{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String AUTHOR = "Autor: Adam Lekan\nData: styczen 2020";
	private static int TRAFFIC = 1000;
	
	
	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu("Menu");
	JMenuItem menuAutor = new JMenuItem("Autor");
	JMenuItem menuExit = new JMenuItem("Wyjdz");
    JPanel panel = new JPanel();
	JLabel limitLabel = new JLabel(" Ograniczenie ruchu:");
	JLabel trafficLabel = new JLabel("                                   Nateżenie ruchu:  ");
	JLabel waitingLabel = new JLabel("                              W kolejce:  ");
	JLabel onBridgeLabel = new JLabel("                             Na moście:  ");

    static JTextArea textArea = new JTextArea(20, 50);
    static JTextField onBridgeField = new JTextField(35);
    static JTextField waitingField = new JTextField(35);
    
    JSlider trafficSlider = new JSlider(JSlider.HORIZONTAL, 500, 5000, TRAFFIC);
    JScrollPane scrollBars = new JScrollPane(textArea, 20, 30);
    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
    @SuppressWarnings("rawtypes")
	JComboBox limitsBox = new JComboBox();

	
	
	public static void main(String[] args) {

	
		new NarrowBridgeConsole();
		
		NarrowBridge bridge = new NarrowBridge();

		while (true) {
	
			Bus bus = new Bus(bridge);
			new Thread(bus).start();
 
			try {
				Thread.sleep(5500 - TRAFFIC);
			} catch (InterruptedException e) {
			}
		}
	}
	 
	
@SuppressWarnings({ "deprecation", "unchecked" })
NarrowBridgeConsole(){
	
    super("Symulacja przejazdu przez wąski most");
    setSize(650, 600);
    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    
    setJMenuBar(menuBar);
    menuBar.add(menu);
    menu.add(menuAutor);
    menu.add(menuExit);
	
    menuAutor.addActionListener(this);
    menuExit.addActionListener(this);
    trafficSlider.addChangeListener(this);
    limitsBox.addActionListener(this);

    trafficSlider.setMajorTickSpacing(500);
    trafficSlider.setPaintTicks(true);
    trafficSlider.setPaintLabels(true);
    
    onBridgeField.setEditable(false);
    waitingField.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    

    labelTable.put( new Integer( 500 ), new JLabel("Małe") );
    labelTable.put( new Integer( 5000 ), new JLabel("Duże") );
    trafficSlider.setLabelTable( labelTable );    

    for(Limits limits :Limits.values()) {
    	limitsBox.addItem(limits);
    	}
    panel.add(limitLabel);
    panel.add(limitsBox);
    panel.add(trafficLabel);
    panel.add(trafficSlider);
    panel.add(onBridgeLabel);
    panel.add(onBridgeField);
    panel.add(waitingLabel);
    panel.add(waitingField);
    panel.add(scrollBars);

    setContentPane(panel);
    setVisible(true);  
}
 
@Override
public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	
	if(source==menuAutor) {
		JOptionPane.showMessageDialog(null, AUTHOR);
	}
	if(source==menuExit) {
		System.exit(0);		
	}

   		switch(limitsBox.getSelectedIndex()) {
   		//no limit
   		case 0:
   			NarrowBridge.busesLimit=Integer.MAX_VALUE;
   			NarrowBridge.unidirection=false;

   			break;
   		//BIDIRECTIONAL,
   		case 1:
   			NarrowBridge.busesLimit=3;
   			NarrowBridge.unidirection=false;

   			break;
   		//UNIDIRECTIONAL,
   		case 2:
   			NarrowBridge.busesLimit=3;
   			NarrowBridge.unidirection=true;
   			
   			break;
   		//ONE_BUS;
   		case 3:
   			NarrowBridge.busesLimit=1;
   			NarrowBridge.unidirection=false;

   			break;
   		}
}
@Override
public void stateChanged(ChangeEvent e) {
	
	 JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	    	TRAFFIC = source.getValue();      
	    }
}	
	
}	
	












