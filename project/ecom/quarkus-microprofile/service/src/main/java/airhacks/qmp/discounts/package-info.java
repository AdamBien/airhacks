@Violation(
    rule = "discount percentage must be between 0 and 100",
    exception = InvalidDiscountPercentageException.class
)
package airhacks.qmp.discounts;

import airhacks.qmp.Violation;
import airhacks.qmp.discounts.entity.InvalidDiscountPercentageException;
