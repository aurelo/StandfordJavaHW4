package count;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class WorkerThread extends Thread {
	
	private JLabel labelToUpdate;
	private Integer countTo;
	
	private final int UPDATE_COUNT = 10000;
	
	class Counter{
		private int previousCounter = 0;
		private int counter;
		
		public void tick(){
			previousCounter = counter;
			counter += UPDATE_COUNT;
		}
		
		public int getCount(){
			return counter;
		}
		
		public int getPreviousCounter(){
			return previousCounter;
		}
		
	};
	
	public WorkerThread(JLabel labelToUpdate, Integer countTo) {
		this.labelToUpdate = labelToUpdate;
		this.countTo = countTo;
	}
	
	@Override
	public void run() {
		try{
			
			final Counter counter = new Counter();
			
			for(int i = 0; i < countTo.intValue(); i++){
				if (i%UPDATE_COUNT == 0) {
					counter.tick();
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							labelToUpdate.setText(String.format("%d", counter.getCount()));
						}
					});
				sleep(100);
				}
				
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					labelToUpdate.setText("Done counting!");
				}
			});
			
		}
		catch (InterruptedException e){
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					labelToUpdate.setText("Stoped!");
				}
			});
		}
		
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
