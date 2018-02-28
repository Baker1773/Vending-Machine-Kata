package main;

public class VendingMachine {

	private double amount;

	public String getDisplay() {
		if (amount != 0)
			return amount + "";
		return "INSERT COIN";
	}

	public void insertCoin(Coin coin) {
		amount += 0.05;
	}

}
