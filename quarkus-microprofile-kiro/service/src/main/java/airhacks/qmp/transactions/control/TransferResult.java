package airhacks.qmp.transactions.control;

import airhacks.qmp.transactions.entity.Transaction;

/**
 * Holds the result of a transfer operation containing both source and destination transactions.
 * Both transactions share the same reference number for traceability.
 */
public record TransferResult(Transaction sourceTransaction, Transaction destinationTransaction) {
}
