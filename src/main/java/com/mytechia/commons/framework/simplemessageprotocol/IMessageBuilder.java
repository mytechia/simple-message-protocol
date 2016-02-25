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

/**
 *  An IMessageBuilder instance knows how to build a kind of Message instances.
 *  It knows its type identifier also (useful to build a MessageFactory).
 *
 * Created by Victor Sonora Pombo <victor.pombo@mytechia.com>.
 */
public interface IMessageBuilder {

    int type();

    Command buildMessage(byte[] msgData);


}
