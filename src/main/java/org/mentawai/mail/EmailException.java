/*
 * Copyright 2001-2004 The Apache Software Foundation
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
package org.mentawai.mail;

// Revision: 191951

/**
 * EmailException
 * @author jakarta-commons
 */
public class EmailException extends Exception {

    public EmailException() {
        super();
    }

    public EmailException(String msg) {
        super(msg);
    }

    public EmailException(String msg, Throwable cause) {
        super(msg, cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public EmailException(Throwable cause) {
        super(cause);
        this.setStackTrace(cause.getStackTrace());
    }

}
