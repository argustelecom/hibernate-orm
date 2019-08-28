package org.hibernate.event.internal;

import org.hibernate.annotations.Immutable;
import org.hibernate.cfg.ForeignKeyCascadeDeleteTestCase;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static org.hibernate.testing.transaction.TransactionUtil.doInHibernate;

/**
 * @author r.aisin
 */
public class RefreshImmutableEntityTestCase extends BaseCoreFunctionalTestCase {

	@Test
	public void testImmutableEntityRefresh() {
		doInHibernate(this::sessionFactory, s -> {
			ResourceIdentity resourceIdentity = new ResourceIdentity();
			s.persist(resourceIdentity);
			s.flush();
			s.refresh(resourceIdentity);
		});
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { ResourceIdentity.class };
	}

	@Immutable
	@Entity(name = "ResourceIdentity")
	@Table(name = "RESOURCE_IDENTITY")
	public static class ResourceIdentity {
		@Id
		@Column(name = "ID")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		Long id;

		public Long getId() {
			return id;
		}
	}

}
