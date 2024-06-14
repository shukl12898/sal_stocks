package shuklaak_CSCI201_Assignment2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

public class Trade extends Thread {

	private static int currentBalance;
	public int stockPrice;
	public int quantity;
	public String ticker;
	private Semaphore semaphore;
	public long startTime;
	private static long zeroTime;
	
	public void setBalance(int originalBalance) {
		currentBalance = originalBalance;
	}
	
	public Trade(int timing, String ticker, int quantity, int stockPrice, Semaphore semaphore) {
		
		this.ticker = ticker;
		this.quantity = quantity;
		this.stockPrice = stockPrice;
		this.startTime = timing;
		this.semaphore = semaphore;
		
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public void setStartTime(long time) {
		zeroTime = time;
	}
	
	static synchronized int updateBalance(int numStocks, int value) {
		
		if (numStocks > 0) {
			currentBalance -= value*numStocks;
		}
		else {
			currentBalance += value*-numStocks;
		}
		
		return currentBalance;
		
	}
	
	public void run(){
		
		DateFormat simple = new SimpleDateFormat("HH:mm:ss:SSS");
		simple.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		//aquire broker lock
		try {
			semaphore.acquire();
			//timestamp start of trade
			long start = System.currentTimeMillis() - zeroTime;
			//if buy
			if (quantity > 0) {
				//check for insufficient balance
				System.out.println("[" + simple.format(start) + "] Starting purchase of " + quantity + " stocks of " + ticker);
				if (currentBalance >= quantity*stockPrice) {
				//if no, update current balance (-$)
					//sleep 2 seconds
					Thread.sleep(2000);
					long end = System.currentTimeMillis() - zeroTime;
					System.out.println("[" + simple.format(end) + "] Finished purchase of " + quantity + " stocks of " + ticker);
					System.out.println("Current Balance after trade: "+ updateBalance(quantity, stockPrice));
				//if yes, transaction failed, exit
				}
				else {
					System.out.println("Transaction failed due to insufficient balance. Unsuccessful purchase of "+ quantity + " stocks of " + ticker);
				}
			}
			//if sell
			else {
				//sleep 3 seconds
				start = new Date().getTime() - zeroTime;
				System.out.println("[" + simple.format(start) + "] Starting sale of " + -quantity + " stocks of " + ticker);
				//update current balance (+$)
				Thread.sleep(3000);
				long end = System.currentTimeMillis() - zeroTime;
				System.out.println("[" + simple.format(end) + "] Finished sale of " + -quantity + " stocks of " + ticker);
				System.out.println("Current Balance after trade: "+ updateBalance(quantity, stockPrice));
			}
			//timestamp end of trade
			//print new balance
		} catch (InterruptedException e) {
			
		}finally {
			//release broker lock
			//update startTime
			semaphore.release();
		}
		
	}
	
}
