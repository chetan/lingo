/**
 *
 * Copyright 2005 LogicBlaze, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/
package org.logicblaze.lingo.jms.marshall;

import org.logicblaze.lingo.LingoInvocation;
import org.logicblaze.lingo.jms.Requestor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @version $Revision: 84 $
 */
public interface Marshaller {

    /**
     * Creates the request message
     *
     * @param requestor
     * @param invocation the remote invocation to send
     * @throws javax.jms.JMSException if the message could not be created
     */
    Message createRequestMessage(Requestor requestor, LingoInvocation invocation) throws JMSException;

    /**
     * Creates the response message
     * 
     * @param session the JMS session to use
     * @param result the result invocation
     * @param requestMessage the original request message
     * @return the response message to send
     * @throws JMSException if the message could not be created
     */
    Message createResponseMessage(Session session, RemoteInvocationResult result, Message requestMessage) throws JMSException;

    /**
     * Extracts the invocation result from the response message
     *
     * @param message the response message
     * @return the invocation result
     * @throws javax.jms.JMSException is thrown if a JMS exception occurs
     */
    RemoteInvocationResult extractInvocationResult(Message message) throws JMSException;

    /**
     * Read a RemoteInvocation from the given JMS message
     *
     * @param message current JMS message
     * @return the RemoteInvocation object
     */
    RemoteInvocation readRemoteInvocation(Message message) throws JMSException;
    
    /**
     * Creates a message for a Java Object for when distributing objects in collections
     */
    Message createObjectMessage(Session session, Object value) throws JMSException;

    /**
     * Extracts the body from the given JMS message
     * @throws JMSException 
     */
    Object readMessage(Message message) throws JMSException;


}
