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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/26/16
 */
public class StringOption extends OptionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String value;

    // Constructors ----------------------------------------------------------------------------------------------------

    public StringOption(char shortLiteral) {
        super(shortLiteral, null);
    }

    public StringOption(char shortLiteral, String longLiteral) {
        super(shortLiteral, longLiteral);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getValue() {
        return value;
    }

    public void setValue(String s) {
        this.value = s;
    }

    @Override
    public String toString() {

        if (getShortLiteral() != 0) {
            return "-" + getShortLiteral() + " \"" + value + "\"";
        }
        else {
            return "???";
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
