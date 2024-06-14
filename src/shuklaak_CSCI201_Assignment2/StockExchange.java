package shuklaak_CSCI201_Assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class StockExchange {
	
	public static void main(String []args) {
		
		String fileName = "";
		Scanner userIn = new Scanner(System.in);
		boolean fileRead = false;
		boolean error = true;
		Stocks stocks = new Stocks();
		
		//large parts taken from Professor Papa's post on Piazza
		while (fileRead == false) {
			
			System.out.println("What is the name of the file containing the company information? ");
			fileName = userIn.nextLine();
			
			try{
				
				error = false;
					
				File file = new File(fileName);
				Scanner sc = new Scanner(file);
				String temp = "";
				
				while (sc.hasNext()) {
					temp += sc.nextLine();
				}
					
				sc.close();
					
				Gson gson = new Gson();
				stocks = gson.fromJson(temp, Stocks.class);
				fileRead = true;
					
			}catch(FileNotFoundException fnfe) {
				
				System.out.println("The file " + fileName + " could not be found.");
				System.out.println();
				error = true;
				continue;
				
				
			}catch(JsonSyntaxException gse) {
				
				System.out.println("The file " + fileName + " is not formatted properly.");
				System.out.println();
				error = true;
				continue;
				
			}
			
			//null fields check
			for (int i = 0; i < stocks.getData().size(); i++) {
				Datum curr = stocks.getDatabyIndex(i);
				if (curr.getName() == null) {
					fileRead = false;
				}
				if (curr.getTicker() == null) {
					fileRead = false;
				}
				if (curr.getStartDate() == null) {
					fileRead = false;
				}
				if (curr.getExchangeCode()==null) {
					fileRead = false;
				}
				if (curr.getDescription()==null) {
					fileRead = false;
				}
				if (curr.getStockBrokers()==0) {
					fileRead = false;
				}
			}
			
			if (error == true) {
				fileRead = false;
				System.out.println("Your file has missing parameters.");
				continue;
			}
			
			fileRead = true;
		
		}
		
		//https://www.javatpoint.com/how-to-read-csv-file-in-java
		System.out.println();
		ArrayList<Trade> tradeCommands = new ArrayList<>();
		fileRead = false;
		error = true;
		int initialBalance = 0;
			
		while (fileRead == false) {
			
			System.out.println("What is the name of the file containing the schedule information? ");
			fileName = userIn.nextLine();
		
			try {
				
				error = false;
				
				File file = new File(fileName);
				Scanner sc = new Scanner(file);			
		
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] splits = line.split(",");
				int endOfList = tradeCommands.size();
				int index = stocks.findTicker(splits[1]);
				Datum data = stocks.getDatabyIndex(index);
				tradeCommands.add(endOfList, new Trade(Integer.parseInt(splits[0]), splits[1], Integer.parseInt(splits[2]), Integer.parseInt(splits[3]), new Semaphore(data.getStockBrokers())));
				
			}
		
			sc.close();
			fileRead = true;
			
			}catch(FileNotFoundException fnfe) {
				
				System.out.println("The file " + fileName + " could not be found.");
				System.out.println();
				error = true;
				continue;
				
			}finally {
				System.out.println();
				System.out.println("What is the Initial Balance?");
				initialBalance = userIn.nextInt();
				
				tradeCommands.get(0).setBalance(initialBalance);
			}
		}
		
		ExecutorService executors = Executors.newCachedThreadPool();
		
		System.out.println();
		
		System.out.println("Initial balance: " + initialBalance);
		
		tradeCommands.get(0).setStartTime(System.currentTimeMillis());
		
		
		//below while loop got help from office hours
		long allocatedTime = 0;
		long start = System.currentTimeMillis();
		
		int i = 0;
		
		while (true) {
			while (i < tradeCommands.size() && (allocatedTime/1000) >= tradeCommands.get(i).getStartTime()){
				executors.execute(tradeCommands.get(i));
				i++;
			}
			if (i == tradeCommands.size()){
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			allocatedTime = System.currentTimeMillis() - start;
		}
		
		executors.shutdown();
		while(!executors.isTerminated()) {
			Thread.yield();
		}
		
		System.out.println("All trades complete!");
		
		userIn.close();
		
	}
}
	
