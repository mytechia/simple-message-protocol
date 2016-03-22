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
 *  An instance of this class represents meta info associated with one message field.
 *
 * Created by Victor Sonora Pombo.
 */
public class MessageFieldInfo {

    private String name = "Unnamed";

    private int sizeInBytes = 0;

    private int fieldIndex = 0;

    private MessageFieldType type = MessageFieldType.BYTE;

    public MessageFieldInfo
            (String name,
             int sizeInBytes,
             int fieldIndex,
             MessageFieldType type) {

        this.name = name;
        this.sizeInBytes = sizeInBytes;
        this.fieldIndex = fieldIndex;
        this.type = type;

    }


    public String getName() {
        return name;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public MessageFieldType getType() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageFieldInfo that = (MessageFieldInfo) o;

        if (sizeInBytes != that.sizeInBytes) return false;
        if (fieldIndex != that.fieldIndex) return false;
        if (!name.equals(that.name)) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + sizeInBytes;
        result = 31 * result + fieldIndex;
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MessageFieldInfo{" +
                "name='" + name + '\'' +
                ", sizeInBytes=" + sizeInBytes +
                ", fieldIndex=" + fieldIndex +
                ", type=" + type +
                '}';
    }
}
