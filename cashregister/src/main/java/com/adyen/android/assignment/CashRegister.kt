package com.adyen.android.assignment

import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.MonetaryElement
import java.util.concurrent.locks.ReentrantLock

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {
    private val lock = ReentrantLock()

    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change {
        if (price < 0) {
            throw IllegalArgumentException("Price cannot be negative.")
        }

        lock.lock()
        try {
            if (amountPaid.total < price) {
                throw TransactionException("Not enough money provided.")
            }

            val changeToGiveAmount = amountPaid.total - price

            // Add the amount paid to the cash register's change
            amountPaid.getElements().forEach { element ->
                val count = amountPaid.getCount(element)
                change.add(element, count)
            }

            // Compute the minimal change to return
            val changeToReturn = computeMinimalChange(changeToGiveAmount)
                ?: run {
                    // Revert the addition if change cannot be provided
                    amountPaid.getElements().forEach { element ->
                        val count = amountPaid.getCount(element)
                        change.remove(element, count)
                    }
                    throw TransactionException("Cannot provide change for the amount: $changeToGiveAmount")
                }

            // Remove the change to return from the cash register's change
            changeToReturn.getElements().forEach { element ->
                val count = changeToReturn.getCount(element)
                change.remove(element, count)
            }

            return changeToReturn
        } finally {
            lock.unlock()
        }
    }

    private fun computeMinimalChange(amount: Long): Change? {
        val maxAmount = amount.toInt()
        val monetaryElements = change.getElements()
            .sortedByDescending { it.minorValue }
            .toList()

        // Initialize arrays to hold minimal coin counts and previous indices

        val minCoins = IntArray(maxAmount + 1) { Int.MAX_VALUE } // An array where minCoins[i] represents the minimal number of coins/bills needed to make up amount i.
        val lastUsed = arrayOfNulls<MonetaryElement>(maxAmount + 1) // An array where lastUsed[i] stores the last monetary element used to reach amount i.

        minCoins[0] = 0

        // Track how many times each monetary element has been used for each amount
        val countsUsed = Array(maxAmount + 1) { mutableMapOf<MonetaryElement, Int>() }

        for (currentAmount in 1..maxAmount) {
            // e.g. currentAmount: €8
            // e.g. monetaryElements: [€1 €2 €5 €10 €20]
            for (monetaryElement in monetaryElements) {
                val monetaryValue = monetaryElement.minorValue
                // e.g. monetaryValue: €5
                if (monetaryValue <= currentAmount) {
                    val prevAmount = currentAmount - monetaryValue // e.g. prevAmount: €3

                    if (minCoins[prevAmount] != Int.MAX_VALUE) {
                        val prevCounts = countsUsed[prevAmount]
                        val usedCount = prevCounts.getOrDefault(monetaryElement, 0)
                        val availableCount = change.getCount(monetaryElement)

                        if (usedCount < availableCount) {
                            val newCoinCount = minCoins[prevAmount] + 1

                            val shouldUpdate = when {
                                newCoinCount < minCoins[currentAmount] -> true
                                newCoinCount == minCoins[currentAmount] -> {
                                    // Prefer larger monetary elements
                                    monetaryElement.minorValue > (lastUsed[currentAmount]?.minorValue ?: 0)
                                }
                                else -> false
                            }

                            if (shouldUpdate) {
                                minCoins[currentAmount] = newCoinCount
                                lastUsed[currentAmount] = monetaryElement

                                // Update counts used
                                countsUsed[currentAmount] = prevCounts.toMutableMap()
                                countsUsed[currentAmount][monetaryElement] = usedCount + 1
                            }
                        }
                    }
                }
            }
        }

        if (minCoins[maxAmount] == Int.MAX_VALUE) {
            return null
        }

        // Build the change to return by backtracking
        val changeToReturn = Change()
        var amountRemaining = maxAmount
        while (amountRemaining > 0) {
            val monetaryElement = lastUsed[amountRemaining]
                ?: throw IllegalStateException("No monetary element found for amount: $amountRemaining")
            changeToReturn.add(monetaryElement, 1)
            amountRemaining -= monetaryElement.minorValue
        }

        return changeToReturn
    }

    class TransactionException(message: String, cause: Throwable? = null) : Exception(message, cause)
}
