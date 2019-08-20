package org.hibernate.cfg;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static org.hibernate.testing.transaction.TransactionUtil.doInHibernate;

/**
 * @author r.aisin
 */
public class ForeignKeyCascadeDeleteTestCase extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { ResourceUnit.class, ResourceIdentity.class };
	}

	@Before
	public void setUp() {
		doInHibernate(this::sessionFactory, s -> {
			ResourceUnit resourceUnit = new ResourceUnit();
			resourceUnit.setIdentity(new ResourceIdentity());
			s.persist(resourceUnit);
		});

	}

	@Test
	public void testCascadeDelete() {
		doInHibernate(this::sessionFactory, s -> {
			ResourceUnit resourceUnit = (ResourceUnit) s.createQuery("select ru from ResourceUnit ru").getResultList()
					.get(0);
			s.delete(resourceUnit);
		});
	}

	@Entity(name = "ResourceIdentity")
	@Table(name = "RESOURCE_IDENTITY")
	@Access(AccessType.FIELD)
	public static class ResourceIdentity {
		Long id;

		@Id
		@Column(name = "ID")
		@Access(AccessType.PROPERTY)
		@GeneratedValue(strategy = GenerationType.SEQUENCE)
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

	@Entity(name = "ResourceUnit")
	@Table(name = "RESOURCE_UNIT")
	@Access(AccessType.FIELD)
	public static class ResourceUnit {

		Long id;

		@Id
		@Column(name = "ID")
		@Access(AccessType.PROPERTY)
		@GeneratedValue(strategy = GenerationType.SEQUENCE)
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		@JoinColumn(name = "id")
		@MapsId
		private ResourceIdentity identity;

		public ResourceIdentity getIdentity() {
			return identity;
		}

		public void setIdentity(ResourceIdentity identity) {
			this.identity = identity;
		}
	}
}
