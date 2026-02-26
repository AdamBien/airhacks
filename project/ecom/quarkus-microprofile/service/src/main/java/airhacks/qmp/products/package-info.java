@Violation(
    rule = "product price must be greater than zero",
    exception = InvalidProductPriceException.class
)
package airhacks.qmp.products;

import airhacks.qmp.Violation;
import airhacks.qmp.products.entity.InvalidProductPriceException;
