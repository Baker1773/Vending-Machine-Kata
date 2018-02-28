package main;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class VendingMachineTest {

	@Test
	public void whenNoCoinsAreInsertedVendingMachinedisplaysINSERTCOIN() {

		VendingMachine vendingMachine = new VendingMachine();
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsNickelByDisplayingCurrentAmount() {

		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.NICKEL);
		assertEquals("0.05", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsDimeByDisplayingCurrentAmount() {

		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.DIME);
		assertEquals("0.10", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsQuarterByDisplayingCurrentAmount() {

		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		assertEquals("0.25", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsMultipleDimesByDisplayingCurrentAmount() {

		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.DIME);
		assertEquals("0.10", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.DIME);
		assertEquals("0.20", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsMultipleNickelsByDisplayingCurrentAmount() {

		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.NICKEL);
		assertEquals("0.05", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.NICKEL);
		assertEquals("0.10", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsMultipleQuartersByDisplayingCurrentAmount() {

		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		assertEquals("0.25", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.QUARTER);
		assertEquals("0.50", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineDoesNotAcceptPennyWhenNoOtherCoinsAreInserted() {

		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.PENNY);
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineDoesNotAcceptPennyWhenOtherCoinsAreInserted() {

		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		assertEquals("0.25", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.PENNY);
		assertEquals("0.25", vendingMachine.getDisplay());
	}

	@Test
	public void whenAPennyIsRejectedItIsPlacedInTheCoinReturn() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.PENNY);
		Map<Coin, Integer> coinsReturned = vendingMachine.checkCoinReturn();
		assertEquals(1, (int) coinsReturned.get(Coin.PENNY));
	}

	@Test
	public void whenTwoPenniesAreInsertedBothAreInTheCoinReturn() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.insertCoin(Coin.PENNY);
		Map<Coin, Integer> coinsReturned = vendingMachine.checkCoinReturn();
		assertEquals(2, (int) coinsReturned.get(Coin.PENNY));
	}

	@Test
	public void newVendingMachinesWillHaveAnEmptyCoinReturn() {
		VendingMachine vendingMachine = new VendingMachine();
		Map<Coin, Integer> coinsReturned = vendingMachine.checkCoinReturn();
		assertEquals(0, coinsReturned.size());
	}

}
