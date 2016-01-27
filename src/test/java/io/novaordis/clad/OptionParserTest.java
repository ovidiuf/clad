/*
 * Copyright (c) 2016 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.clad;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/26/16
 */
public class OptionParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(OptionParserTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        List<Option> options = OptionParser.parse(0, Collections.emptyList());
        assertTrue(options.isEmpty());
    }

    @Test
    public void parse_DashByItself() throws Exception {

        List<String> args = new ArrayList<>(Collections.singletonList("-"));

        try {

            OptionParser.parse(0, args);
            fail("should have thrown UserErrorException");
        }
        catch(UserErrorException e) {
            String msg = e.getMessage();
            log.info(e.getMessage());
            assertEquals("invalid option \"-\"", msg);
        }
    }

    @Test
    public void parse_ShortLiteral_BooleanValue() throws Exception {

        List<String> args = new ArrayList<>(Collections.singletonList("-t"));

        List<Option> options = OptionParser.parse(0, args);

        assertTrue(args.isEmpty());

        assertEquals(1, options.size());

        Option option = options.get(0);

        assertEquals('t', option.getShortLiteral());

        BooleanOption bo = (BooleanOption)option;

        assertTrue(bo.getValue());
    }

    @Test
    public void parse_ShortLiteral_StringValue() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("-t", "test"));

        List<Option> options = OptionParser.parse(0, args);

        assertTrue(args.isEmpty());

        assertEquals(1, options.size());

        StringOption so = (StringOption)options.get(0);

        assertEquals('t', so.getShortLiteral());

        assertEquals("test", so.getValue());
    }

    // typeHeuristics() ------------------------------------------------------------------------------------------------

    @Test
    public void typeHeuristics_null() throws Exception {

        assertNull(OptionParser.typeHeuristics(null));
    }

    @Test
    public void typeHeuristics_Long() throws Exception {

        Long value = (Long)OptionParser.typeHeuristics("1");
        assertEquals(1L, value.longValue());
    }

    @Test
    public void typeHeuristics_Double() throws Exception {

        Double value = (Double)OptionParser.typeHeuristics("1.1");
        assertEquals(1.1, value.doubleValue(), 0.00001);
    }

    @Test
    public void typeHeuristics_True() throws Exception {

        Boolean value = (Boolean)OptionParser.typeHeuristics("true");
        assertTrue(value);

        value = (Boolean)OptionParser.typeHeuristics("True");
        assertTrue(value);

        value = (Boolean)OptionParser.typeHeuristics("TruE");
        assertTrue(value);

        value = (Boolean)OptionParser.typeHeuristics("TRUE");
        assertTrue(value);
    }

    @Test
    public void typeHeuristics_FALSE() throws Exception {

        Boolean value = (Boolean)OptionParser.typeHeuristics("false");
        assertFalse(value);

        value = (Boolean)OptionParser.typeHeuristics("False");
        assertFalse(value);

        value = (Boolean)OptionParser.typeHeuristics("FalsE");
        assertFalse(value);

        value = (Boolean)OptionParser.typeHeuristics("FALSE");
        assertFalse(value);
    }

    @Test
    public void typeHeuristics_String() throws Exception {

        String value = (String)OptionParser.typeHeuristics("something");
        assertEquals("something", value);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
