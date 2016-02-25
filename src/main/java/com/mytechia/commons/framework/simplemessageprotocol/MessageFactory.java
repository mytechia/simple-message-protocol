/*******************************************************************************
 * Copyright (C) 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 * Copyright (C) 2016 Victor Sonora Pombo <victor.pombo@mytechia.com>
 * <p>
 * This file is part of simple-message-protocol.
 * <p>
 * simple-message-protocol is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * simple-message-protocol is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with simple-message-protocol.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.mytechia.commons.framework.simplemessageprotocol;

import com.mytechia.commons.framework.simplemessageprotocol.exception.MessageFormatException;

import java.util.HashMap;
import java.util.Map;

/**
 *  One instance of this class handles a map of message builder instances
 *  and it uses those builders to transform between byte[] and Command instances.
 *
 * Created by Victor Sonora Pombo <victor.pombo@mytechia.com>.
 */
public class MessageFactory {

    private Map<Integer, IMessageBuilder> messageBuilders = new HashMap<>();


    public void registerMessageBuilder(IMessageBuilder messageBuilder) {
        this.messageBuilders.put(messageBuilder.type(), messageBuilder);
    }


    public Command decodeMessage(byte[] messageData) throws MessageFormatException {

        byte msgType = Command.getMessageType(messageData);

        final IMessageBuilder messageBuilder = messageBuilders.get(msgType);

        if (null != messageBuilder) {
            return messageBuilder.buildMessage(messageData);
        }

        return null;

    }


}
