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

import com.thoughtworks.xstream.XStream;

import org.logicblaze.lingo.LingoInvocation;
import org.logicblaze.lingo.jms.Requestor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Uses XStream to marshall requests and responses into and out of messages.
 * 
 * @version $Revision: 84 $
 */
public class XStreamMarshaller extends DefaultMarshaller {
    private XStream xStream;

    public Message createRequestMessage(Requestor requestor, LingoInvocation invocation) throws JMSException {
        String xml = toXML(invocation);
        return requestor.getSession().createTextMessage(xml);
    }

    public Message createResponseMessage(Session session, RemoteInvocationResult result, Message requestMessage)
            throws JMSException {
        String xml = toXML(result);
        TextMessage message = session.createTextMessage(xml);
        appendMessageHeaders(message, session, result);
        return message;
    }

    public RemoteInvocationResult extractInvocationResult(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            return (RemoteInvocationResult) fromXML(text);
        }
        return super.extractInvocationResult(message);
    }

    public RemoteInvocation readRemoteInvocation(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            return (RemoteInvocation) fromXML(text);
        }
        return super.readRemoteInvocation(message);
    }
    
    public Message createObjectMessage(Session session, Object value) throws JMSException {
        String xml = toXML(value);
        TextMessage message = session.createTextMessage(xml);
        appendMessageHeaders(message, session, value);
        return message;
    }

    public Object readMessage(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            return fromXML(text);
        }
        return super.readMessage(message);
    }

    // Properties
    // -------------------------------------------------------------------------
    public XStream getXStream() {
        if (xStream == null) {
            xStream = createXStream();
        }
        return xStream;
    }

    public void setXStream(XStream xStream) {
        this.xStream = xStream;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected XStream createXStream() {
        XStream answer = new XStream();
        answer.alias("invoke", LingoInvocation.class);
        answer.alias("result", RemoteInvocationResult.class);
        return answer;
    }

    protected Object fromXML(String xml) {
        return getXStream().fromXML(xml);
    }

    protected String toXML(Object object) {
        return getXStream().toXML(object);
    }

}
