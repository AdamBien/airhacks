package airhacks.qmp.discounts.control;

import static java.lang.System.Logger.Level.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import airhacks.qmp.discounts.entity.Discount;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;

@ApplicationScoped
public class DiscountStore {

    static System.Logger LOG = System.getLogger(DiscountStore.class.getName());

    @Inject
    EntityManager em;

    public List<Discount> all() {
        LOG.log(INFO, "returning all discounts");
        return this.em.createNamedQuery("Discount.all", Discount.class).getResultList();
    }

    public List<Discount> activeDiscounts() {
        LOG.log(INFO, "returning active discounts");
        return this.em.createNamedQuery("Discount.active", Discount.class)
                .setParameter("today", LocalDate.now())
                .getResultList();
    }

    public Discount add(Discount discount) {
        LOG.log(INFO, "adding discount: " + discount.code());
        this.em.persist(discount);
        return discount;
    }

    public Optional<Discount> findById(String discountId) {
        LOG.log(INFO, "finding discount by id: " + discountId);
        return Optional.ofNullable(this.em.find(Discount.class, discountId));
    }

    public Optional<Discount> findByCode(String code) {
        LOG.log(INFO, "finding discount by code: " + code);
        var results = this.em.createQuery("SELECT d FROM Discount d WHERE d.code = :code", Discount.class)
                .setParameter("code", code)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
