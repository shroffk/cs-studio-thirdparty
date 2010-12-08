/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.alarm.beast.ui.globalclientmodel;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import org.csstudio.alarm.beast.AlarmTreePath;
import org.csstudio.alarm.beast.JMSCommunicationThread;
import org.csstudio.alarm.beast.Preferences;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmUpdateInfo;
import org.csstudio.platform.logging.CentralLogger;

/** JMS communicator that receives 'global' alarm updates
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
abstract public class GlobalAlarmCommunicator extends JMSCommunicationThread
{
    private MessageConsumer consumer;

    public GlobalAlarmCommunicator(final String url)
    {
        super(url);
    }

    @Override
    protected void createProducersAndConsumers() throws Exception
    {
        consumer = createConsumer(Preferences.getJMS_GlobalServerTopic());
        consumer.setMessageListener(new MessageListener()
        {
            public void onMessage(final Message message)
            {
                if (message instanceof MapMessage)
                    handleMapMessage((MapMessage) message);
                else
                    CentralLogger.getInstance().getLogger(this).warn(
                        "Message type " + message.getClass().getName() + " not handled");
            }
        });
    }

    @Override
    protected void closeProducersAndConsumers() throws Exception
    {
        consumer.close();
        consumer = null;
    }

    /** Handler for received MapMessages
     *  @param message JMS MapMessage
     */
    private void handleMapMessage(final MapMessage message)
    {
        try
        {
            final AlarmUpdateInfo info = AlarmUpdateInfo.fromMapMessage(message);
            if (AlarmTreePath.isPath(info.getNameOrPath()))
                handleAlarmUpdate(info);
            else
                CentralLogger.getInstance().getLogger(this).warn(
                        "Received global update for '" + info.getNameOrPath() + "', no path");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /** Called for received alarm updates
     *  @param info Update info
     */
    abstract void handleAlarmUpdate(AlarmUpdateInfo info);
}
