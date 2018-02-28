package main;

public class VendingMachine {

	private double amount;

	public String getDisplay() {
		if (amount != 0)
			return String.format("%.2f", amount);
		return "INSERT COIN";
	}

	public void insertCoin(Coin coin) {
		switch (coin) {

		case NICKEL:
			amount += 0.05;
			break;

		case DIME:
			amount += 0.10;
			break;

		case QUARTER:
			amount += 0.25;
			break;

		default:
			break;
		}
	}

}
