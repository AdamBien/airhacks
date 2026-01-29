package airhacks.qmp.transactions.control;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import airhacks.qmp.accounts.entity.Currency;

/**
 * Currency conversion functionality using exchange rates relative to USD as base currency.
 * Rates represent how many units of each currency equal 1 USD.
 */
public interface ExchangeRates {

    /**
     * Exchange rates relative to USD (1 USD = X units of currency).
     * For example: EUR rate of 0.92 means 1 USD = 0.92 EUR.
     */
    Map<Currency, BigDecimal> RATES_TO_USD = Map.of(
        Currency.USD, BigDecimal.ONE,
        Currency.EUR, new BigDecimal("0.92"),
        Currency.GBP, new BigDecimal("0.79"),
        Currency.JPY, new BigDecimal("149.50"),
        Currency.CHF, new BigDecimal("0.88"),
        Currency.CAD, new BigDecimal("1.36"),
        Currency.AUD, new BigDecimal("1.53"),
        Currency.CNY, new BigDecimal("7.24"),
        Currency.INR, new BigDecimal("83.12"),
        Currency.SGD, new BigDecimal("1.34")
    );

    /**
     * Converts an amount from one currency to another using current exchange rates.
     *
     * @param amount the amount to convert
     * @param from   source currency
     * @param to     target currency
     * @return converted amount with 2 decimal places
     */
    static BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (from == to) {
            return amount;
        }
        var exchangeRate = rate(from, to);
        return amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns the exchange rate from one currency to another.
     * Calculation: (1 / fromRate) * toRate
     * Example: EUR to GBP = (1 / 0.92) * 0.79 = 0.8587
     *
     * @param from source currency
     * @param to   target currency
     * @return exchange rate with 6 decimal precision
     */
    static BigDecimal rate(Currency from, Currency to) {
        if (from == to) {
            return BigDecimal.ONE;
        }
        var fromRate = RATES_TO_USD.get(from);
        var toRate = RATES_TO_USD.get(to);
        // Convert from -> USD -> to
        // fromAmount in USD = fromAmount / fromRate
        // toAmount = fromAmountInUSD * toRate
        return toRate.divide(fromRate, 6, RoundingMode.HALF_UP);
    }

    /**
     * Checks if a currency is supported for exchange operations.
     *
     * @param currency the currency to check
     * @return true if the currency has a configured exchange rate
     */
    static boolean isSupported(Currency currency) {
        return RATES_TO_USD.containsKey(currency);
    }
}
