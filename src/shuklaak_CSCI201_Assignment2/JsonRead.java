package shuklaak_CSCI201_Assignment2;

import java.util.ArrayList;
import java.util.Comparator;
import org.apache.commons.lang3.text.WordUtils;

class Datum {

	private String name;
	private String ticker;
	private String startDate;
	private int stockBrokers = 0;
	private String description;
	private String exchangeCode;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public int getStockBrokers() {
		return stockBrokers;
	}
	
	public void setStockBrokers(int stockBrokers) {
		this.stockBrokers = stockBrokers;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExchangeCode() {
		return exchangeCode;
	}
	
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

}

class Stocks {

	private ArrayList<Datum> data = null;
	
	public ArrayList<Datum> getData() {
		return data;
	}
	
	public void setData(ArrayList<Datum> data) {
		this.data = data;
	}
	
	public Datum getDatabyIndex(int index) {
		
		return this.data.get(index);
	}
	
	public void printDatabyIndex(int index) {
		
		String result = "";
		result += this.data.get(index).getName();
		result += ", symbol ";
		result += this.data.get(index).getTicker();
		result += ", started on ";
		result += this.data.get(index).getStartDate();
		result += ", listed on ";
		result += this.data.get(index).getExchangeCode();
		
		System.out.println(result);
		
	} 
	
	public void printDescriptionbyIndex(int index) {
		
		String result = "";
		result += this.data.get(index).getName();
		result += ", symbol ";
		result += this.data.get(index).getTicker();
		result += ", started on ";
		result += this.data.get(index).getStartDate();
		result += ", listed on ";
		result += this.data.get(index).getExchangeCode();
		result += ",";
		
		System.out.println(result);
		
		String description = WordUtils.wrap(this.data.get(index).getDescription(),60, "\n\t", true);
		System.out.println("\t" + description);

	}
	
	public int findTicker(String ticker) {
		
		int i = 0;
		while (i < this.data.size()) {
			if (ticker.equalsIgnoreCase(this.data.get(i).getTicker())) {
				return i;
			}
			else {
				i++;
			}
		}
		
		return -1;
		
	}
	
	public int findName(String name) {
		
		int i = 0;
		while (i < this.data.size()) {
			if (name.equalsIgnoreCase(this.data.get(i).getName())) {
				return i;
			}
			else {
				i++;
			}
		}
		
		return -1;
		
	}
	
	public ArrayList<String> findExchangeStocks(String exchange){
		
		int i = 0;
		ArrayList<String> result = new ArrayList<String>();
		
		while (i < this.data.size()) {
			if (exchange.equalsIgnoreCase(this.data.get(i).getExchangeCode())) {
				result.add(this.data.get(i).getTicker());
			}
			
			i++;
			
		}
		
		return result;
		
	}
	
	//below taken from https://www.geeksforgeeks.org/how-to-sort-an-arraylist-of-objects-by-property-in-java/
	public static Comparator<Datum> atozCompare = new Comparator<Datum>() {
		      
		        public int compare(Datum s1, Datum s2) {
		            String stockName1 = s1.getName().toUpperCase();
		            String stockName2 = s2.getName().toUpperCase();
		 
		            return stockName1.compareTo(stockName2);
		  
		        }
			
			};
			
}