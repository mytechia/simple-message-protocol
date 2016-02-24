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
import com.mytechia.commons.util.conversion.EndianConversor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mytechia.commons.framework.simplemessageprotocol.Command.writeStringInStream;

/**
 *  A Builder-like class that handles Message fields to byte[] coding.
 *  For each field it adds a MessageFieldInfo instance.
 *  There is a utility method to add a new field for each data type used.
 *
 * Created by Victor Sonora Pombo <victor.pombo@mytechia.com>.
 */
public class MessageCoder {

    private List<MessageFieldInfo> messageFieldInfoList = new ArrayList<>();

    private int nextFieldIndex = 1;

    private ByteArrayOutputStream dataStream = new ByteArrayOutputStream();

    private Endianness endianness = Endianness.LITTLE_ENDIAN;


    public MessageCoder(Endianness endianness) {
        this.reset();
        this.endianness = endianness;
    }


    private void reset() {
        this.messageFieldInfoList = new ArrayList<>();
        this.nextFieldIndex = 1;
        this.dataStream = new ByteArrayOutputStream();
    }


    public List<MessageFieldInfo> getMessageFieldInfo() {
        return this.messageFieldInfoList;
    }


    public MessageCoder writeByte(byte data, String name) {
        this.dataStream.write(data);
        messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        1,
                        this.nextFieldIndex++,
                        MessageFieldType.BYTE
                ));
        return this;
    }


    public MessageCoder writeShort(short data, String name) throws MessageFormatException {
        byte[] dataArray = new byte[EndianConversor.SHORT_SIZE_BYTES];

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            EndianConversor.shortToLittleEndian(data, dataArray, 0);
        } else {
            EndianConversor.shortToBigEndian(data, dataArray, 0);
        }

        try {
            this.dataStream.write(dataArray);
        } catch (IOException e) {
            throw new MessageFormatException(e, this.getClass().getName());
        }

        messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.SHORT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.SHORT
                ));

        return this;
    }


    public MessageCoder writeUShort(short data, String name) throws MessageFormatException {
        byte[] dataArray = new byte[EndianConversor.SHORT_SIZE_BYTES];

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            EndianConversor.ushortToLittleEndian(data, dataArray, 0);
        } else {
            throw new MessageFormatException("Not supported");
        }

        try {
            this.dataStream.write(dataArray);
        } catch (IOException e) {
            throw new MessageFormatException(e, this.getClass().getName());
        }

        messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.SHORT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.USHORT
                ));

        return this;
    }


    public MessageCoder writeInt(int data, String name) throws MessageFormatException {
        byte[] dataArray = new byte[EndianConversor.INT_SIZE_BYTES];

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            EndianConversor.intToLittleEndian(data, dataArray, 0);
        } else {
            EndianConversor.intToBigEndian(data, dataArray, 0);
        }

        try {
            this.dataStream.write(dataArray);
        } catch (IOException e) {
            throw new MessageFormatException(e, this.getClass().getName());
        }

        messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.INT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.INT
                ));

        return this;
    }


    public MessageCoder writeUInt(int data, String name) throws MessageFormatException {
        byte[] dataArray = new byte[EndianConversor.INT_SIZE_BYTES];

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            EndianConversor.uintToLittleEndian(data, dataArray, 0);
        } else {
            throw new MessageFormatException("Not supported");
        }

        try {
            this.dataStream.write(dataArray);
        } catch (IOException e) {
            throw new MessageFormatException(e, this.getClass().getName());
        }

        messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.INT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.UINT
                ));

        return this;
    }


    public MessageCoder writeLong(long data, String name) throws MessageFormatException {
        byte[] dataArray = new byte[EndianConversor.LONG_SIZE_BYTES];

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            EndianConversor.longToLittleEndian(data, dataArray, 0);
        } else {
            EndianConversor.longToBigEndian(data, dataArray, 0);
        }

        try {
            this.dataStream.write(dataArray);
        } catch (IOException e) {
            throw new MessageFormatException(e, this.getClass().getName());
        }

        messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.LONG_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.LONG
                ));

        return this;
    }


    public MessageCoder writeString (String data, String name) throws MessageFormatException {
        try {
            final int lengthInBytes = writeStringInStream(this.dataStream, data);

            messageFieldInfoList.add(
                    new MessageFieldInfo(
                            name,
                            lengthInBytes,
                            this.nextFieldIndex++,
                            MessageFieldType.STRING
                    ));

        } catch (IOException e) {
            throw new MessageFormatException(e, this.getClass().getName());
        }

        return this;
    }


    public MessageCoder writeByteArray (byte [] data, String name) throws MessageFormatException {

        try {
            this.dataStream.write(data);
        } catch (IOException e) {
            throw new MessageFormatException(e, this.getClass().getName());
        }

        messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        data.length,
                        this.nextFieldIndex++,
                        MessageFieldType.BYTEARRAY
                ));

        return this;

    }


    public MessageCoder writeByteArrayWithSize (byte [] data, String name) throws MessageFormatException {

        byte[] lenData = new byte[2];
        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            EndianConversor.shortToLittleEndian((short) data.length, lenData, 0);
        } else {
            EndianConversor.shortToBigEndian((short) data.length, lenData, 0);
        }

        try {

            this.dataStream.write(lenData);
            this.dataStream.write(data);

        } catch (IOException e) {
            throw new MessageFormatException(e, this.getClass().getName());
        }

        messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        data.length + EndianConversor.SHORT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.BYTEARRAY
                ));

        return this;

    }


}
