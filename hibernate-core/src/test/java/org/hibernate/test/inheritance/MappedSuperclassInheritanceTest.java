/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.inheritance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.cfg.AnnotationBinder;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.jpa.test.BaseEntityManagerFunctionalTestCase;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.logger.LoggerInspectionRule;
import org.hibernate.testing.logger.Triggerable;
import org.junit.Rule;
import org.junit.Test;

import org.jboss.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Vlad Mihalcea
 */
@TestForIssue(jiraKey = "HHH-12653")
public class MappedSuperclassInheritanceTest extends BaseEntityManagerFunctionalTestCase {

	@Rule
	public LoggerInspectionRule logInspection = new LoggerInspectionRule( Logger.getMessageLogger( CoreMessageLogger.class, AnnotationBinder.class.getName() ) );

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] {
				Employee.class,
				Manager.class,
				Developer.class
		};
	}

	@Override
	public void buildEntityManagerFactory() {
		Triggerable triggerable = logInspection.watchForLogMessages( "HHH000503:" );
		triggerable.reset();
		assertFalse( triggerable.wasTriggered() );

		super.buildEntityManagerFactory();

		assertTrue( triggerable.wasTriggered() );
		assertTrue( triggerable.triggerMessage().contains( "An entity cannot be annotated with both @Inheritance and @MappedSuperclass" ) );
	}

	@Test
	public void test() {

	}

	@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
	@MappedSuperclass
	public static class Employee {

		@Id
		@GeneratedValue
		private Long id;

		private String jobType;

		private String firstName;

		private String lastName;
	}

	@Entity(name = "Manager")
	public static class Manager extends Employee {
	}

	@Entity(name = "Developer")
	public static class Developer extends Employee {
	}
}
