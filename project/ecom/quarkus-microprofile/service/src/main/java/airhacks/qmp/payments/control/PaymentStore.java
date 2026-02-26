package airhacks.qmp.payments.control;

import static java.lang.System.Logger.Level.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import airhacks.qmp.payments.entity.Payment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentStore {

    static System.Logger LOG = System.getLogger(PaymentStore.class.getName());

    List<Payment> payments = new CopyOnWriteArrayList<>();

    public List<Payment> all() {
        LOG.log(INFO, "returning all payments");
        return this.payments;
    }

    public Payment add(Payment payment) {
        LOG.log(INFO, "adding payment: " + payment.amount());
        this.payments.add(payment);
        return payment;
    }

    public List<Payment> findByCustomerId(String customerId) {
        LOG.log(INFO, "finding payments for customer: " + customerId);
        return this.payments.stream()
                .filter(p -> p.customerId().equals(customerId))
                .toList();
    }
}
