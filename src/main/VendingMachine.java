package main;

import java.util.Map;
import java.util.TreeMap;

public class VendingMachine {

	private double amount;
	private Map<Coin, Integer> coinReturn;

	public VendingMachine() {
		amount = 0;
		coinReturn = new TreeMap<Coin, Integer>();
	}

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

		case PENNY:
			int pennyCount = 0;
			if (coinReturn.containsKey(Coin.PENNY))
				pennyCount = coinReturn.get(Coin.PENNY);
			coinReturn.put(Coin.PENNY, pennyCount + 1);
			break;

		default:
			break;
		}
	}

	public Map<Coin, Integer> emptyCoinReturn() {
		Map<Coin, Integer> coinReturned = new TreeMap<Coin, Integer>();
		coinReturned.putAll(coinReturn);
		coinReturn.clear();
		return coinReturned;
	}

	public void pressReturnCoinButton() {
		coinReturn.put(Coin.QUARTER, 1);
	}

}
