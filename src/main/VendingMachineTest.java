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
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, (int) coinsReturned.get(Coin.PENNY));
	}

	@Test
	public void whenTwoPenniesAreInsertedBothAreInTheCoinReturn() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.insertCoin(Coin.PENNY);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(2, (int) coinsReturned.get(Coin.PENNY));
	}

	@Test
	public void newVendingMachinesWillHaveAnEmptyCoinReturn() {
		VendingMachine vendingMachine = new VendingMachine();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(0, coinsReturned.size());
	}

	@Test
	public void whenCoinReturnIsCheckedItIsEmptied() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.insertCoin(Coin.PENNY);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(2, (int) coinsReturned.get(Coin.PENNY));
		coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(0, coinsReturned.size());
	}

	@Test
	public void insertingAQuarterThenPressingTheReturnButtonWillReturnAQuarter() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void pressingTheReturnButtonWillReturnMultipleCoins() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(3, coinsReturned.size());
		assertEquals(2, (int) coinsReturned.get(Coin.QUARTER));
		assertEquals(3, (int) coinsReturned.get(Coin.DIME));
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));
	}

	@Test
	public void coinsReturnedFromCoinReturnButtonWillCombineWithRejectedPennies() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(4, coinsReturned.size());
		assertEquals(2, (int) coinsReturned.get(Coin.QUARTER));
		assertEquals(3, (int) coinsReturned.get(Coin.DIME));
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));
		assertEquals(2, (int) coinsReturned.get(Coin.PENNY));
	}

	@Test
	public void insertingAQuarterThenPressingTheReturnButtonWillResetTheDisplay() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void pressingTheReturnCoinsButtonTwiceInARowWillOnlyReturnTheCoinsOnce() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void insertingAQuarterPressingReturnAndInsertertingAnotherQuarterAndPressingReturnWillResultInTwoQuartersInTheCoinReturn() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(2, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void insertingAQuarterThenReturningAndEmpyingTheCoinReturnTwiceWillEndUpWithOneQuarterEachTime() {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
	}
}
