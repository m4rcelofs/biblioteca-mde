package erep.mde.dao;

import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

import erep.mde.data.Payment;

public class PaymentDAO extends AbstractDAO<Payment> {

	@SuppressWarnings("unchecked")
	public Collection<Payment> getUnpaid() {
		Query q = pm.newQuery(Payment.class);
		q.setFilter("paidOn == null");
		return pm.detachCopyAll((List<Payment>) q.execute());
	}

}
