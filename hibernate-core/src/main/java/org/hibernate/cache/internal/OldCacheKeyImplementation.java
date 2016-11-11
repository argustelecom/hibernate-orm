/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.cache.internal;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.util.compare.EqualsHelper;
import org.hibernate.type.Type;

/**
 * Allows multiple entity classes / collection roles to be stored in the same cache region. Also allows for composite
 * keys which do not properly implement equals()/hashCode().
 *
 * This was named org.hibernate.cache.spi.CacheKey in Hibernate until version 5.
 * Temporarily maintained as a reference while all components catch up with the refactoring to the caching interfaces.
 *
 * @author Gavin King
 * @author Steve Ebersole
 *
 * @deprecated In optimized implementations, wrapping the id is not necessary.
 */
@Deprecated
final class OldCacheKeyImplementation implements Serializable {
	private final Object id;
	private final String typeName;
	private final String entityOrRoleName;
	private final String tenantId;
	private final int hashCode;

	/**
	 * Construct a new key for a collection or entity instance.
	 * Note that an entity name should always be the root entity
	 * name, not a subclass entity name.
	 *
	 * @param id The identifier associated with the cached data
	 * @param type The Hibernate type mapping
	 * @param entityOrRoleName The entity or collection-role name.
	 * @param tenantId The tenant identifier associated this data.
	 * @param factory The session factory for which we are caching
	 */
	OldCacheKeyImplementation(
			final Object id,
			final String typeName,
			final String entityOrRoleName,
			final String tenantId,
			final SessionFactoryImplementor factory) {
		this.id = id;
		this.typeName = typeName;
		this.entityOrRoleName = entityOrRoleName;
		this.tenantId = tenantId;
		this.hashCode = 51 + ((id == null)? 0: id.hashCode());
	}

	public Object getId() {
		return id;
	}

		@Override
		public boolean equals(Object other) {
			
			if (other == null) {
				return false;
			}
			if (this == other) {
				return true;
			}
			if (hashCode != other.hashCode() || !(other instanceof OldCacheKeyImplementation)) {
				// hashCode is part of this check since it is pre-calculated and hash must match for equals to be true
				return false;
			}
			final OldCacheKeyImplementation that = (OldCacheKeyImplementation) other;
			// если мы здесь, значит объекты одного класса (this) и имеют одинаковый hash. По идее, можно уже
			// возвращать true, но уточним еще кое-что:
			return EqualsHelper.equals(entityOrRoleName, that.entityOrRoleName) && EqualsHelper.equals(typeName, that.typeName) && EqualsHelper.equals( id, that.id);
		}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		// Used to be required for OSCache
		return entityOrRoleName + '#' + id.toString();
	}
}
