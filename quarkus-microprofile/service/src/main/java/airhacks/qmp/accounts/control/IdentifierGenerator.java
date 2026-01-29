package airhacks.qmp.accounts.control;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import airhacks.qmp.accounts.entity.AccountIdentifier;
import airhacks.qmp.accounts.entity.AsiaPacificIdentifier;
import airhacks.qmp.accounts.entity.IbanIdentifier;
import airhacks.qmp.accounts.entity.IndiaIdentifier;
import airhacks.qmp.accounts.entity.Region;
import airhacks.qmp.accounts.entity.UsIdentifier;

/**
 * Generates region-specific account identifiers.
 * Uses static methods for stateless identifier generation.
 */
interface IdentifierGenerator {
    
    /**
     * Generates an account identifier for the specified region.
     * 
     * @param region the geographic region
     * @param bankCode the bank code used for identifier generation
     * @return a region-specific account identifier
     */
    static AccountIdentifier generate(Region region, String bankCode) {
        return switch (region) {
            case EUROPE -> generateIban("DE", bankCode);
            case INDIA -> generateIndiaIdentifier(bankCode);
            case US -> generateUsIdentifier(bankCode);
            case ASIA_PACIFIC -> generateAsiaPacificIdentifier(bankCode);
        };
    }
    
    /**
     * Generates an IBAN for European accounts following ISO 13616 format.
     * Format: 2-letter country code + 2 check digits + up to 30 alphanumeric characters (BBAN)
     * 
     * @param countryCode ISO 3166-1 alpha-2 country code
     * @param bankCode the bank identifier code
     * @return an IbanIdentifier with valid IBAN format
     */
    static IbanIdentifier generateIban(String countryCode, String bankCode) {
        var bban = generateBban(bankCode);
        var checkDigits = calculateIbanCheckDigits(countryCode, bban);
        var iban = countryCode.toUpperCase() + checkDigits + bban;
        return new IbanIdentifier(iban);
    }
    
    /**
     * Generates an identifier for Indian accounts with IFSC code.
     * 
     * @param ifscCode the Indian Financial System Code
     * @return an IndiaIdentifier with unique account number
     */
    static IndiaIdentifier generateIndiaIdentifier(String ifscCode) {
        var accountNumber = generateNumericAccountNumber(14);
        return new IndiaIdentifier(accountNumber, ifscCode);
    }
    
    /**
     * Generates an identifier for US accounts with routing number.
     * 
     * @param routingNumber the ABA routing transit number
     * @return a UsIdentifier with unique account number
     */
    static UsIdentifier generateUsIdentifier(String routingNumber) {
        var accountNumber = generateNumericAccountNumber(12);
        return new UsIdentifier(accountNumber, routingNumber);
    }
    
    /**
     * Generates an identifier for Asia-Pacific accounts with bank code.
     * 
     * @param bankCode the regional bank code
     * @return an AsiaPacificIdentifier with unique account number
     */
    static AsiaPacificIdentifier generateAsiaPacificIdentifier(String bankCode) {
        var accountNumber = generateNumericAccountNumber(16);
        return new AsiaPacificIdentifier(accountNumber, bankCode);
    }
    
    /**
     * Generates the Basic Bank Account Number (BBAN) portion of an IBAN.
     * Uses bank code followed by unique account digits.
     */
    private static String generateBban(String bankCode) {
        var sanitizedBankCode = bankCode.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        var paddedBankCode = padOrTruncate(sanitizedBankCode, 8);
        var accountPart = generateNumericAccountNumber(10);
        return paddedBankCode + accountPart;
    }
    
    /**
     * Calculates IBAN check digits using MOD-97 algorithm (ISO 7064).
     */
    private static String calculateIbanCheckDigits(String countryCode, String bban) {
        var rearranged = bban + convertLettersToDigits(countryCode) + "00";
        var remainder = mod97(rearranged);
        var checkDigits = 98 - remainder;
        return String.format("%02d", checkDigits);
    }
    
    /**
     * Converts letters to their numeric representation for IBAN calculation.
     * A=10, B=11, ..., Z=35
     */
    private static String convertLettersToDigits(String input) {
        var result = new StringBuilder();
        for (char c : input.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) {
                result.append(c - 'A' + 10);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    /**
     * Calculates MOD 97 for large numbers represented as strings.
     */
    private static int mod97(String number) {
        var remainder = 0;
        for (int i = 0; i < number.length(); i++) {
            var digit = Character.getNumericValue(number.charAt(i));
            remainder = (remainder * 10 + digit) % 97;
        }
        return remainder;
    }
    
    /**
     * Generates a unique numeric account number of specified length.
     */
    private static String generateNumericAccountNumber(int length) {
        var uuid = UUID.randomUUID().toString().replaceAll("-", "");
        var numericPart = new StringBuilder();
        for (char c : uuid.toCharArray()) {
            if (Character.isDigit(c)) {
                numericPart.append(c);
            } else {
                numericPart.append(Character.getNumericValue(c) % 10);
            }
            if (numericPart.length() >= length) {
                break;
            }
        }
        while (numericPart.length() < length) {
            numericPart.append(ThreadLocalRandom.current().nextInt(10));
        }
        return numericPart.substring(0, length);
    }
    
    /**
     * Pads or truncates a string to the specified length.
     */
    private static String padOrTruncate(String input, int length) {
        if (input.length() >= length) {
            return input.substring(0, length);
        }
        return String.format("%-" + length + "s", input).replace(' ', '0');
    }
}
