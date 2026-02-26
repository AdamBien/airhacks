@Violation(
    rule = "customer age must not exceed 90",
    exception = CustomerTooOldException.class
)
package airhacks.qmp.customers;

import airhacks.qmp.Violation;
import airhacks.qmp.customers.entity.CustomerTooOldException;
