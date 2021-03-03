import java.util.LinkedList;
import java.util.List;

class NarrowBridge {
	
	public List<Bus> allBuses = new LinkedList<Bus>();
	public static List<Bus> busesWaiting = new LinkedList<Bus>();
	public static List<Bus> busesOnTheBridge = new LinkedList<Bus>();
	public static int busesLimit = Integer.MAX_VALUE;
	public static boolean unidirection=false;
	BusDirection lastBusDirection = null;
	
	public int sameDir=0;
	
	
	
	void printBridgeInfo(Bus bus, String message){
		
		StringBuilder sb = new StringBuilder();
		String text = NarrowBridgeConsole.textArea.getText();
		sb.append("Bus["+bus.id+"->"+bus.dir+"]  ");
		sb.append(message+"\n");
		NarrowBridgeConsole.textArea.setText(sb.toString() + text);
		System.out.println(sb);
		
		sb.setLength(0);
		for(Bus b: busesWaiting) 
			sb.append(b.id + " "); 
			NarrowBridgeConsole.waitingField.setText(sb.toString());
			System.out.println(sb);

		sb.setLength(0);
		for(Bus b: busesOnTheBridge) 
			sb.append(b.id + " "); 
			NarrowBridgeConsole.onBridgeField.setText(sb.toString());
			System.out.println(sb);
			}

	 
	synchronized void getOnTheBridge(Bus bus){
		 
		

		if(!busesOnTheBridge.isEmpty()) {
			if(lastBusDirection == bus.dir)
				++sameDir;
			lastBusDirection=((LinkedList<Bus>) busesOnTheBridge).getLast().dir;}
		else { 
			lastBusDirection = bus.dir;}

		
		while(sameDir >= 10)
		{
			if(lastBusDirection==BusDirection.WEST)
				lastBusDirection=BusDirection.EAST;
			else
				lastBusDirection=BusDirection.WEST;
			sameDir=0;
		}

		//System.out.println("licz:"+sameDir);
		
		while((busesOnTheBridge.size()>=busesLimit) || ((lastBusDirection != bus.dir) && unidirection))
		{

			busesWaiting.add(bus);
			printBridgeInfo(bus, "CZEKA NA WJAZD");
			try { 
				wait();
			} catch (InterruptedException e) { }
			busesWaiting.remove(bus);
		} 
				
		busesOnTheBridge.add(bus);
		printBridgeInfo(bus, "WJEŻDŻA NA MOST");
		
	}
	
	synchronized void getOffTheBridge(Bus bus){

		busesOnTheBridge.remove(bus);
		printBridgeInfo(bus, "OPUSZCZA MOST");


		notify();
	}

}  
