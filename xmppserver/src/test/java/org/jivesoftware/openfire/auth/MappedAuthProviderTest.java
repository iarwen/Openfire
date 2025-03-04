/*
 * Copyright (C) 2024 Ignite Realtime Foundation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.openfire.auth;

import org.jivesoftware.Fixtures;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.JiveGlobals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests that verify the implementation of {@link MappedAuthProvider}
 *
 * @author Guus der Kinderen, guus@goodbytes.nl
 */
public class MappedAuthProviderTest
{
    @BeforeAll
    public static void setup() throws Exception {
        Fixtures.reconfigureOpenfireHome();
        Fixtures.disableDatabasePersistence();
    }

    @BeforeEach
    @AfterEach
    public void unsetProperties() {
        JiveGlobals.deleteProperty(MappedAuthProvider.PROPERTY_MAPPER_CLASSNAME);
    }

    /**
     * Verifies that a password from user 'jane' can be retrieved (from the secondary provider)
     */
    @Test
    public void testGetJanePassword() throws Exception
    {
        // Setup test fixture.
        JiveGlobals.setProperty(MappedAuthProvider.PROPERTY_MAPPER_CLASSNAME, TestAuthProviderMapper.class.getName());
        final MappedAuthProvider provider = new MappedAuthProvider();

        // Execute system under test.
        final String result = provider.getPassword("jane");

        // Verify results.
        assertEquals("secret", result);
    }

    /**
     * Verifies that a password from user 'john' can be retrieved (from the tertiary provider)
     */
    @Test
    public void testGetJohnPassword() throws Exception
    {
        // Setup test fixture.
        JiveGlobals.setProperty(MappedAuthProvider.PROPERTY_MAPPER_CLASSNAME, TestAuthProviderMapper.class.getName());
        final MappedAuthProvider provider = new MappedAuthProvider();

        // Execute system under test.
        final String result = provider.getPassword("john");

        // Verify results.
        assertEquals("secret", result);
    }

    /**
     * Verifies that an exception is thrown when a password for a non-existing user is being requested.
     */
    @Test()
    public void testGetNonExistingAuth() throws Exception
    {
        // Setup test fixture.
        JiveGlobals.setProperty(MappedAuthProvider.PROPERTY_MAPPER_CLASSNAME, TestAuthProviderMapper.class.getName());
        final MappedAuthProvider provider = new MappedAuthProvider();

        // Execute system under test & Verify results.
        assertThrows(UserNotFoundException.class, () -> provider.getPassword("non-existing-user"));
    }
}
