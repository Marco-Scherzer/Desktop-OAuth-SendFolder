package com.marcoscherzer.msimplegoauthmailserviceapplication.util;
/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MMutableBoolean {
        private boolean value;

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MMutableBoolean(boolean value) {
            this.value = value;
        }

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public final boolean get() {
            return value;
        }

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public final void set(boolean value) {
            this.value = value;
        }
    }


