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
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

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

    private static List<String> tokenizeCommandLine(String commandLine) {
        List<String> result = new ArrayList<>();
        for(StringTokenizer st = new StringTokenizer(commandLine, " "); st.hasMoreTokens(); ) {
            result.add(st.nextToken());
        }
        return result;
    }

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

        List<String> args = tokenizeCommandLine("-");

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

        List<String> args = tokenizeCommandLine("-t");

        List<Option> options = OptionParser.parse(0, args);

        assertTrue(args.isEmpty());

        assertEquals(1, options.size());

        Option option = options.get(0);

        assertEquals('t', option.getShortLiteral().charValue());

        BooleanOption bo = (BooleanOption)option;

        assertTrue(bo.getValue());
    }

    @Test
    public void parse_ShortLiteral_StringValue() throws Exception {

        List<String> args = tokenizeCommandLine("-t test");

        List<Option> options = OptionParser.parse(0, args);

        assertTrue(args.isEmpty());

        assertEquals(1, options.size());

        StringOption so = (StringOption)options.get(0);

        assertEquals('t', so.getShortLiteral().charValue());

        assertEquals("test", so.getValue());
    }

    @Test
    public void parseOptions() throws Exception {

        List<String> args = tokenizeCommandLine("global1 global2 -c command-value --command2=command2-value");

        List<Option> options = OptionParser.parse(2, args);

        assertEquals(2, args.size());
        assertEquals("global1", args.get(0));
        assertEquals("global2", args.get(1));

        assertEquals(2, options.size());

        StringOption option = (StringOption)options.get(0);

        assertEquals('c', option.getShortLiteral().charValue());
        assertNull(option.getLongLiteral());
        assertEquals("command-value", option.getValue());

        option = (StringOption)options.get(1);

        assertNull(option.getShortLiteral());
        assertEquals("command2", option.getLongLiteral());
        assertEquals("command2-value", option.getValue());
    }

    @Test
    public void parse_HandleDoubleQuotes() throws Exception {

        List<String> args = tokenizeCommandLine("-f \"something something else\"");

        List<Option> options = OptionParser.parse(0, args);

        assertTrue(args.isEmpty());

        assertEquals(1, options.size());
        StringOption option = (StringOption)options.get(0);
        assertEquals('f', option.getShortLiteral().charValue());
        assertNull(option.getLongLiteral());
        assertEquals("something something else", option.getValue());
    }

    @Test
    public void parse_HandleSingleQuotes() throws Exception {

        List<String> args = tokenizeCommandLine("-f 'something something else'");

        List<Option> options = OptionParser.parse(0, args);

        assertTrue(args.isEmpty());

        assertEquals(1, options.size());
        StringOption option = (StringOption)options.get(0);
        assertEquals('f', option.getShortLiteral().charValue());
        assertNull(option.getLongLiteral());
        assertEquals("something something else", option.getValue());
    }

    // handleQuotes ----------------------------------------------------------------------------------------------------

    @Test
    public void handleDoubleQuotes_UnbalancedDoubleQuotes() throws Exception {

        List<String> args = tokenizeCommandLine("a \"b \"c");

        try {
            OptionParser.handleQuotes(1, args);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {
            log.info(e.getMessage());
        }

        assertEquals(3, args.size());
        assertEquals("a", args.get(0));
        assertEquals("\"b", args.get(1));
        assertEquals("\"c", args.get(2));
    }

    @Test
    public void handleSingleQuotes_UnbalancedSingleQuotes() throws Exception {

        List<String> args = tokenizeCommandLine("a 'b 'c");

        try {
            OptionParser.handleQuotes(1, args);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {
            log.info(e.getMessage());
        }

        assertEquals(3, args.size());
        assertEquals("a", args.get(0));
        assertEquals("'b", args.get(1));
        assertEquals("'c", args.get(2));
    }

    @Test
    public void handleDoubleQuotes_UnbalancedDoubleQuotes2() throws Exception {

        List<String> args = tokenizeCommandLine("a \"b c \"d");

        try {
            OptionParser.handleQuotes(1, args);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {
            log.info(e.getMessage());
        }

        assertEquals(4, args.size());
        assertEquals("a", args.get(0));
        assertEquals("\"b", args.get(1));
        assertEquals("c", args.get(2));
        assertEquals("\"d", args.get(3));
    }

    @Test
    public void handleSingleQuotes_UnbalancedSingleQuotes2() throws Exception {

        List<String> args = tokenizeCommandLine("a 'b c 'd");

        try {
            OptionParser.handleQuotes(1, args);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {
            log.info(e.getMessage());
        }

        assertEquals(4, args.size());
        assertEquals("a", args.get(0));
        assertEquals("'b", args.get(1));
        assertEquals("c", args.get(2));
        assertEquals("'d", args.get(3));
    }

    @Test
    public void handleDoubleQuotes() throws Exception {

        List<String> args = tokenizeCommandLine("a \"b c\"");

        OptionParser.handleQuotes(0, args);

        assertEquals(2, args.size());
        assertEquals("a", args.get(0));
        assertEquals("b c", args.get(1));
    }

    @Test
    public void handleSingleQuotes() throws Exception {

        List<String> args = tokenizeCommandLine("a 'b c'");

        OptionParser.handleQuotes(0, args);

        assertEquals(2, args.size());
        assertEquals("a", args.get(0));
        assertEquals("b c", args.get(1));
    }

    @Test
    public void handleDoubleQuotes2() throws Exception {

        List<String> args = tokenizeCommandLine("a \"b c\"");
        OptionParser.handleQuotes(1, args);

        assertEquals(2, args.size());
        assertEquals("a", args.get(0));
        assertEquals("b c", args.get(1));
    }

    @Test
    public void handleSingleQuotes2() throws Exception {

        List<String> args = tokenizeCommandLine("a 'b c'");
        OptionParser.handleQuotes(1, args);

        assertEquals(2, args.size());
        assertEquals("a", args.get(0));
        assertEquals("b c", args.get(1));
    }

    @Test
    public void handleDoubleQuotes3() throws Exception {

        List<String> args = tokenizeCommandLine("a \"b c d\"");

        OptionParser.handleQuotes(1, args);

        assertEquals(2, args.size());
        assertEquals("a", args.get(0));
        assertEquals("b c d", args.get(1));
    }

    @Test
    public void handleSingleQuotes3() throws Exception {

        List<String> args = tokenizeCommandLine("a 'b c d'");

        OptionParser.handleQuotes(1, args);

        assertEquals(2, args.size());
        assertEquals("a", args.get(0));
        assertEquals("b c d", args.get(1));
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

    // parseLongLiteralOption() ----------------------------------------------------------------------------------------

    @Test
    public void pastLongLiteralOption_Null() throws Exception {

        try {
            OptionParser.parseLongLiteralOption(null);
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void pastLongLiteralOption_DoesNotStartWithDashDash() throws Exception {

        try {
            OptionParser.parseLongLiteralOption("-something");
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void pastLongLiteralOption() throws Exception {

        StringOption option = (StringOption)OptionParser.parseLongLiteralOption("--option=option-value");

        assertEquals("option", option.getLongLiteral());
        assertNull(option.getShortLiteral());
        assertEquals("option-value", option.getValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
