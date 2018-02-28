package main;

import java.util.Map;
import java.util.TreeMap;

public class VendingMachine {

	private double amount;
	private Map<Coin, Integer> coinReturn;
	private Map<Coin, Integer> insertedCoins;

	boolean pressedCola;
	boolean pressedChips;

	public VendingMachine() {
		amount = 0;
		coinReturn = new TreeMap<Coin, Integer>();
		insertedCoins = new TreeMap<Coin, Integer>();
		pressedCola = false;
	}

	public String getDisplay() {
		if (pressedCola) {
			pressedCola = false;
			return "PRICE $1.00";
		}

		if (pressedChips) {
			pressedChips = false;
			return "PRICE $0.50";
		}

		if (amount != 0)
			return String.format("%.2f", amount);
		return "INSERT COIN";
	}

	public void insertCoin(Coin coin) {

		int coinCount = 0;
		if (insertedCoins.containsKey(coin))
			coinCount = insertedCoins.get(coin);

		switch (coin) {

		case NICKEL:
			amount += 0.05;
			insertedCoins.put(Coin.NICKEL, coinCount + 1);
			break;

		case DIME:
			amount += 0.10;
			insertedCoins.put(Coin.DIME, coinCount + 1);
			break;

		case QUARTER:
			amount += 0.25;
			insertedCoins.put(Coin.QUARTER, coinCount + 1);
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
		for (Coin c : insertedCoins.keySet()) {
			if (coinReturn.containsKey(c))
				coinReturn.put(c, insertedCoins.get(c) + coinReturn.get(c));
			else
				coinReturn.put(c, insertedCoins.get(c));
		}
		insertedCoins.clear();
		amount = 0;
	}

	public void selectProduct(Product product) {

		if (product == Product.COLA)
			pressedCola = true;
		else
			pressedChips = true;

	}

}
