package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CashRegisterTest {
    @Test
    fun testTransaction() {
        // Initialize the cash register with some change
        val initialChange = Change().apply {
            add(Bill.TWENTY_EURO, 2)
            add(Bill.FIVE_EURO, 5)
            add(Coin.TWO_EURO, 10)
            add(Coin.ONE_EURO, 20)
            add(Coin.FIFTY_CENT, 50)
        }

        val cashRegister = CashRegister(initialChange)

        // Customer wants to buy an item priced at 73.50 EURO
        val price = 73_50L

        // Customer pays with 100 EURO (2 x 50 EURO bills)
        val amountPaid = Change().apply {
            add(Bill.FIFTY_EURO, 2)
        }

        // Perform the transaction
        val changeReturned = cashRegister.performTransaction(price, amountPaid)

        // Expected change to return: 26.50 EURO
        val expectedChange = Change().apply {
            add(Bill.TWENTY_EURO, 1)
            add(Bill.FIVE_EURO, 1)
            add(Coin.ONE_EURO, 1)
            add(Coin.FIFTY_CENT, 1)
        }

        // Verify that the change returned is as expected
        assertEquals(expectedChange, changeReturned)
    }

    @Test
    fun testExactPayment() {
        // Initialize the cash register with some change
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 5)
            add(Coin.ONE_EURO, 10)
        }

        val cashRegister = CashRegister(initialChange)

        // Item price is 10 EURO
        val price = 10_00L

        // Customer pays exactly 10 EURO
        val amountPaid = Change().apply {
            add(Bill.TEN_EURO, 1)
        }

        // Perform the transaction
        val changeReturned = cashRegister.performTransaction(price, amountPaid)

        // Expected change is none
        val expectedChange = Change.none()

        // Verify that no change is returned
        assertEquals(expectedChange, changeReturned)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun testInsufficientPayment() {
        // Initialize the cash register with some change
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 1)
            add(Coin.ONE_EURO, 5)
        }

        val cashRegister = CashRegister(initialChange)

        // Item price is 20 EURO
        val price = 20_00L

        // Customer pays only 10 EURO
        val amountPaid = Change().apply {
            add(Bill.TEN_EURO, 1)
        }

        // Perform the transaction (should throw TransactionException)
        cashRegister.performTransaction(price, amountPaid)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun testNoChangeAvailable() {
        // Initialize the cash register with limited change
        val initialChange = Change().apply {
            add(Bill.FIVE_EURO, 1)
            add(Coin.ONE_EURO, 1)
        }

        val cashRegister = CashRegister(initialChange)

        // Item price is 8 EURO
        val price = 8_00L

        // Customer pays with a 10 EURO bill
        val amountPaid = Change().apply {
            add(Bill.TEN_EURO, 1)
        }

        // Perform the transaction (should throw TransactionException due to insufficient change)
        cashRegister.performTransaction(price, amountPaid)
    }

    @Test
    fun testMultipleTransactions() {
        // Initialize the cash register with sufficient monetary elements
        val initialChange = Change().apply {
            add(Bill.TWENTY_EURO, 1)
            add(Bill.TEN_EURO, 2)
            add(Bill.FIVE_EURO, 5)
            add(Coin.TWO_EURO, 10)
            add(Coin.ONE_EURO, 20)
            add(Coin.FIFTY_CENT, 50)
        }

        val cashRegister = CashRegister(initialChange)

        // First transaction
        val price = 15_50L
        val amountPaid = Change().apply {
            add(Bill.TWENTY_EURO, 1)
        }
        val changeReturned = cashRegister.performTransaction(price, amountPaid)
        val expectedChange = Change().apply {
            add(Coin.TWO_EURO, 2)
            add(Coin.FIFTY_CENT, 1)
        }
        assertEquals(expectedChange, changeReturned)

        // Second transaction
        val price2 = 7_00L
        val amountPaid2 = Change().apply {
            add(Bill.TEN_EURO, 1)
        }
        val changeReturned2 = cashRegister.performTransaction(price2, amountPaid2)
        val expectedChange2 = Change().apply {
            add(Coin.TWO_EURO, 1)
            add(Coin.ONE_EURO, 1)
        }
        assertEquals(expectedChange2, changeReturned2)

        // Third transaction with exact payment
        val price3 = 5_00L
        val amountPaid3 = Change().apply {
            add(Bill.FIVE_EURO, 1)
        }
        val changeReturned3 = cashRegister.performTransaction(price3, amountPaid3)
        val expectedChange3 = Change.none()
        assertEquals(expectedChange3, changeReturned3)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun testUnavailableMonetaryElement() {
        // Initialize the cash register with no coins
        val initialChange = Change().apply {
            add(Bill.FIFTY_EURO, 1)
            add(Bill.TWENTY_EURO, 1)
            add(Bill.TEN_EURO, 1)
        }

        val cashRegister = CashRegister(initialChange)

        // Item price is 35 EURO
        val price = 35_00L

        // Customer pays with 50 EURO
        val amountPaid = Change().apply {
            add(Bill.FIFTY_EURO, 1)
        }

        // Perform the transaction (should throw TransactionException due to no smaller bills or coins for change)
        cashRegister.performTransaction(price, amountPaid)
    }

    @Test
    fun testZeroAmountTransaction() {
        // Initialize the cash register with some change
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 1)
            add(Coin.ONE_EURO, 5)
        }

        val cashRegister = CashRegister(initialChange)

        // Item price is 0 EURO (free item)
        val price = 0L

        // Customer pays nothing
        val amountPaid = Change.none()

        // Perform the transaction
        val changeReturned = cashRegister.performTransaction(price, amountPaid)

        // Expected change is none
        val expectedChange = Change.none()

        // Verify that no change is returned
        assertEquals(expectedChange, changeReturned)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativePrice() {
        // Initialize the cash register with some change
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 2)
        }

        val cashRegister = CashRegister(initialChange)

        // Negative price (invalid)
        val price = -10_00L

        // Customer pays with 10 EURO
        val amountPaid = Change().apply {
            add(Bill.TEN_EURO, 1)
        }

        // Perform the transaction (should throw IllegalArgumentException)
        cashRegister.performTransaction(price, amountPaid)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativePayment() {
        // Initialize the cash register with some change
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 2)
        }

        val cashRegister = CashRegister(initialChange)

        // Item price is 10 EURO
        val price = 10_00L

        // Negative payment (invalid)
        val amountPaid = Change().apply {
            add(Bill.TEN_EURO, -1) // Invalid negative count
        }

        // Perform the transaction (should throw IllegalArgumentException)
        cashRegister.performTransaction(price, amountPaid)
    }

    @Test
    fun testLargeAmountTransaction() {
        // Initialize the cash register with large monetary elements
        val initialChange = Change().apply {
            add(Bill.FIVE_HUNDRED_EURO, 2)
            add(Bill.TWO_HUNDRED_EURO, 5)
            add(Bill.ONE_HUNDRED_EURO, 10)
            add(Bill.FIFTY_EURO, 20)
            add(Bill.TWENTY_EURO, 10) // Added 20 EURO bills
        }

        val cashRegister = CashRegister(initialChange)

        // Item price is 1230 EURO
        val price = 1230_00L

        // Customer pays with 2000 EURO
        val amountPaid = Change().apply {
            add(Bill.FIVE_HUNDRED_EURO, 4)
        }

        // Perform the transaction
        val changeReturned = cashRegister.performTransaction(price, amountPaid)

        // Expected change is 770 EURO
        val expectedChange = Change().apply {
            add(Bill.FIVE_HUNDRED_EURO, 1)
            add(Bill.TWO_HUNDRED_EURO, 1)
            add(Bill.FIFTY_EURO, 1)
            add(Bill.TWENTY_EURO, 1)
        }

        // Verify that the change returned is as expected
        assertEquals(expectedChange, changeReturned)
    }

    @Test
    fun testConcurrentTransactions() {
        // Initialize the cash register with ample change
        val initialChange = Change().apply {
            Bill.entries.forEach { add(it, 10) }
            Coin.entries.forEach { add(it, 50) }
        }

        val cashRegister = CashRegister(initialChange)

        // Define a runnable task for transactions
        val transactionTask = Runnable {
            val price = 15_50L
            val amountPaid = Change().apply {
                add(Bill.TWENTY_EURO, 1)
            }
            val changeReturned = cashRegister.performTransaction(price, amountPaid)
            val expectedChange = Change().apply {
                add(Bill.FIVE_EURO, 1)
                add(Coin.FIFTY_CENT, 1)
            }
            assertEquals(expectedChange, changeReturned)
        }

        // Run multiple transactions concurrently
        val threads = List(10) { Thread(transactionTask) }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
    }
}
