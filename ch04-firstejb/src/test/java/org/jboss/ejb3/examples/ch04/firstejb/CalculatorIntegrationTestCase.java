/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.ejb3.examples.ch04.firstejb;

import java.net.MalformedURLException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * CalculatorIntegrationTestCase
 * 
 * Integration tests for the CalculatorEJB exposing one 
 * remote business view
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
@RunWith(Arquillian.class)
public class CalculatorIntegrationTestCase
{
   // ---------------------------------------------------------------------------||
   // Class Members -------------------------------------------------------------||
   // ---------------------------------------------------------------------------||

   /**
    * Logger
    */
   private static final Logger log = Logger.getLogger(CalculatorIntegrationTestCase.class);

   /**
    * The JNDI Naming Context
    */
   private static Context namingContext;

   /**
    * The EJB 3.x local business view of the CalculatorEJB
    */
   private static CalculatorLocalBusiness calcLocalBusiness;

   /**
    * Delegate for ensuring that the obtained Calculators are working as expected
    */
   private static CalculatorAssertionDelegate assertionDelegate;

   /**
    * JNDI Name of the Remote Business Reference
    */
   //TODO Use Global JNDI Syntax 
   private static final String JNDI_NAME_CALC_LOCAL_BUSINESS = ManyViewCalculatorBean.class.getSimpleName() + "Local";

   /**
    * Define the deployment
    */
   @Deployment
   public static JavaArchive createDeployment() throws MalformedURLException
   {
      final JavaArchive archive = Archives.create("firstejb.jar", JavaArchive.class).addPackage(
            CalculatorBeanBase.class.getPackage());
      log.info(archive.toString(true));
      return archive;
   }

   // ---------------------------------------------------------------------------||
   // Lifecycle Methods ---------------------------------------------------------||
   // ---------------------------------------------------------------------------||

   /**
    * Run once before any tests
    */
   @BeforeClass
   public static void beforeClass() throws Throwable
   {
      // Create the naming context, using jndi.properties on the CP
      namingContext = new InitialContext();

      // Obtain EJB 3.x Business Reference
      calcLocalBusiness = (CalculatorLocalBusiness) namingContext.lookup(JNDI_NAME_CALC_LOCAL_BUSINESS);

      // Create Assertion Delegate
      assertionDelegate = new CalculatorAssertionDelegate();
   }

   // ---------------------------------------------------------------------------||
   // Tests ---------------------------------------------------------------------||
   // ---------------------------------------------------------------------------||

   /**
    * Ensures that the CalculatorEJB adds as expected,
    * using the EJB 3.x business view
    */
   @Test
   public void testAdditionUsingBusinessReference() throws Throwable
   {
      // Test 
      log.info("Testing remote business reference...");
      assertionDelegate.assertAdditionSucceeds(calcLocalBusiness);
   }

}
