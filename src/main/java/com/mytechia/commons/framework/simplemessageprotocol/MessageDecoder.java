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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *  A Builder-like class that handles byte[] to Message fields decoding.
 *  For each field it adds a MessageFieldInfo instance.
 *  There is a utility method to add a new field for each data type used.
 *
 * Created by Victor Sonora Pombo <victor.pombo@mytechia.com>.
 */
public class MessageDecoder {

    public static final int MAX_SIZE_IN_BYTES = 512;

    private List<MessageFieldInfo> messageFieldInfoList = new ArrayList<>();

    private int nextFieldIndex = 1;

    private Endianness endianness = Endianness.LITTLE_ENDIAN;

    private byte[] dataArray = null;

    private int arrayIndex = 0;


    public MessageDecoder(Endianness endianness, byte[] dataArray, int arrayIndex) {
        this.reset();
        this.endianness = endianness;
        this.dataArray = dataArray;
        this.arrayIndex = arrayIndex;
    }


    public MessageDecoder(Endianness endianness, byte[] dataArray) {
        this(endianness, dataArray, 0);
    }


    private void reset() {
        this.messageFieldInfoList = new ArrayList<>();
        this.nextFieldIndex = 1;
        this.arrayIndex = 0;
    }


    public List<MessageFieldInfo> getMessageFieldInfo() {
        return this.messageFieldInfoList;
    }


    public int getArrayIndex() {
        return this.arrayIndex;
    }


    public byte readByte(String name) {
        this.messageFieldInfoList.add(
            new MessageFieldInfo(
                    name,
                    1,
                    this.nextFieldIndex++,
                    MessageFieldType.BYTE
            )
        );

        return this.dataArray[this.arrayIndex++];
    }


    public short readShort(String name) {

        short data = 0;

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            data = EndianConversor.byteArrayLittleEndianToShort(this.dataArray, this.arrayIndex);
        } else {
            data = EndianConversor.byteArrayBigEndianToShort(this.dataArray, this.arrayIndex);
        }

        this.arrayIndex += EndianConversor.SHORT_SIZE_BYTES;

        this.messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.SHORT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.SHORT
                )
        );

        return data;

    }


    public int readUShort(String name) throws MessageFormatException {

        int data = 0;

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            data = EndianConversor.byteArrayLittleEndianToUShort(this.dataArray, this.arrayIndex);
        } else {
            throw new MessageFormatException("Not supported");
        }

        this.arrayIndex += EndianConversor.INT_SIZE_BYTES;

        this.messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.INT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.USHORT
                )
        );

        return data;

    }


    public int readInt(String name) {

        int data = 0;

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            data = EndianConversor.byteArrayLittleEndianToInt(this.dataArray, this.arrayIndex);
        } else {
            data = EndianConversor.byteArrayBigEndianToInt(this.dataArray, this.arrayIndex);
        }

        this.arrayIndex += EndianConversor.INT_SIZE_BYTES;

        this.messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.INT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.INT
                )
        );

        return data;

    }


    public long readLong(String name) {

        long data = 0;

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            data = EndianConversor.byteArrayLittleEndianToLong(this.dataArray, this.arrayIndex);
        } else {
            data = EndianConversor.byteArrayBigEndianToLong(this.dataArray, this.arrayIndex);
        }

        this.arrayIndex += EndianConversor.LONG_SIZE_BYTES;

        this.messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.LONG_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.LONG
                )
        );

        return data;

    }


    public double readDouble(String name) {

        double data = 0;

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            data = EndianConversor.byteArrayLittleEndianToDouble(this.dataArray, this.arrayIndex);
        } else {
            data = EndianConversor.byteArrayBigEndianToDouble(this.dataArray, this.arrayIndex);
        }

        this.arrayIndex += EndianConversor.LONG_SIZE_BYTES;

        this.messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        EndianConversor.LONG_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.DOUBLE
                )
        );

        return data;

    }


    public String readString(String name) throws MessageFormatException {

        StringBuilder stringBuilder = new StringBuilder(50);
        int size = Command.readStringFromBytes(stringBuilder, this.dataArray, this.arrayIndex);

        this.arrayIndex += size;

        this.messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        size,
                        this.nextFieldIndex++,
                        MessageFieldType.STRING
                )
        );

        return stringBuilder.toString();

    }


    public byte [] readByteArray(String name, int size) {

        byte [] data = Arrays.copyOfRange(this.dataArray, this.arrayIndex, this.arrayIndex + size);

        this.arrayIndex += size;

        this.messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        size,
                        this.nextFieldIndex++,
                        MessageFieldType.BYTEARRAY
                )
        );

        return data;

    }


    public byte [] readByteArray(String name) {

        short lenData = 0;

        if (this.endianness == Endianness.LITTLE_ENDIAN) {
            lenData = EndianConversor.byteArrayLittleEndianToShort(this.dataArray, this.arrayIndex);
        } else {
            lenData = EndianConversor.byteArrayBigEndianToShort(this.dataArray, this.arrayIndex);
        }

        this.arrayIndex += EndianConversor.SHORT_SIZE_BYTES;

        byte [] data = Arrays.copyOfRange(this.dataArray, this.arrayIndex, this.arrayIndex + lenData);

        this.arrayIndex += lenData;

        this.messageFieldInfoList.add(
                new MessageFieldInfo(
                        name,
                        lenData + EndianConversor.SHORT_SIZE_BYTES,
                        this.nextFieldIndex++,
                        MessageFieldType.BYTEARRAY
                )
        );

        return data;

    }


}
