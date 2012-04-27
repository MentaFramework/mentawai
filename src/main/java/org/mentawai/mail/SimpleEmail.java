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

import org.mentawai.util.StringUtils;

// Revision: 155415

/**
 * This class is used to send simple internet email messages without
 * attachments.
 *
 * @author <a href="mailto:quintonm@bellsouth.net">Quinton McCombs</a>
 * @author <a href="mailto:colin.chalmers@maxware.nl">Colin Chalmers</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:frank.kim@clearink.com">Frank Y. Kim</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:unknown">Regis Koenig</a>
 * @version $Id: SimpleEmail.java,v 1.1 2006/03/02 17:11:41 soliveira Exp $
 */
public class SimpleEmail extends Email {
    
    public SimpleEmail() {
        super();
    }

    /**
     * Sets the content of the mail.
     *
     * @param messageBody A String.
     * @return An Email.
     * @throws EmailException see javax.mail.internet.MimeBodyPart
     *  for defintions
     */
    public Email setMsg(String messageBody) throws EmailException {
        if (StringUtils.isEmpty(messageBody)) {
            throw new EmailException("The message cannot be empty.");
        }

        setContent(messageBody, TEXT_PLAIN);
        return this;
    }
    
    public static void sendNow(String toName, String toEmail, String subject, String msg) throws Exception {
    	SimpleEmail email = new SimpleEmail();
        email.addTo(toEmail, toName);
        email.setSubject(subject);
        email.setMsg(msg);
        email.send();
    }
    
    public static void sendLater(String toName, String toEmail, String subject, String msg) throws Exception {
    	SimpleEmail email = new SimpleEmail();
        email.addTo(toEmail, toName);
        email.setSubject(subject);
        email.setMsg(msg);
        email.sendLater();
    }
    
    public static void sendNow(String fromName, String fromEmail, String toName, String toEmail, String subject, String msg) throws Exception {
    	SimpleEmail email = new SimpleEmail();
        email.addTo(toEmail, toName);
        email.setFrom(fromEmail, fromName);
        email.setSubject(subject);
        email.setMsg(msg);
        email.send();
    }
    
    public static void sendLater(String fromName, String fromEmail, String toName, String toEmail, String subject, String msg) throws Exception {
    	SimpleEmail email = new SimpleEmail();
        email.addTo(toEmail, toName);
        email.setFrom(fromEmail, fromName);
        email.setSubject(subject);
        email.setMsg(msg);
        email.sendLater();
    }
}