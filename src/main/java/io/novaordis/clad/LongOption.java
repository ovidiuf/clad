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
public class LongOption extends OptionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Long value;

    // Constructors ----------------------------------------------------------------------------------------------------

    public LongOption(Character shortLiteral) {
        this(shortLiteral, null);
    }

    public LongOption(String longLiteral) {
        this(null, longLiteral);
    }

    public LongOption(Character shortLiteral, String longLiteral) {
        super(shortLiteral, longLiteral);
    }

    // OptionBase override ---------------------------------------------------------------------------------------------

    @Override
    public Long getValue() {
        return value;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setValue(Long l) {
        this.value = l;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}