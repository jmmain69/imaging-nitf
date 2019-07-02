/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */
package org.codice.imaging.nitf.trewrap.fields;

import java.io.InputStream;
import java.util.Arrays;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Tests for field lookup
 */
public class SimpleLookupTest {

    TestLogger LOGGER = TestLoggerFactory.getTestLogger(SimpleLookup.class);

    public SimpleLookupTest() {
    }

    @Test
    public void checkBadResourcePath() {
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));
        try (InputStream stream = SimpleLookup.class.getResourceAsStream("/bad path");) {
            SimpleLookup lookup = new SimpleLookup(stream);
            assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(LoggingEvent.warn("Problem parsing XML for null:null. javax.xml.stream.XMLStreamException: java.net.MalformedURLException"))));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
