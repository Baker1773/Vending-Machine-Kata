package main;

public class VendingMachine {

	private double amount;

	public String getDisplay() {
		if (amount != 0)
			return String.format("%.2f", amount);
		return "INSERT COIN";
	}

	public void insertCoin(Coin coin) {
		if (Coin.DIME == coin)
			amount = 0.10;
		else
			amount += 0.05;
	}

}
