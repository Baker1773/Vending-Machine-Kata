package main;

public class VendingMachine {

	private double amount;

	public String getDisplay() {
		if (amount != 0)
			return String.format("%.2f", amount);
		return "INSERT COIN";
	}

	public void insertCoin(Coin coin) {
		if (coin.QUARTER == coin)
			amount += 0.25;
		else if (Coin.DIME == coin)
			amount += 0.10;
		else if (Coin.NICKEL == coin)
			amount += 0.05;
	}

}
