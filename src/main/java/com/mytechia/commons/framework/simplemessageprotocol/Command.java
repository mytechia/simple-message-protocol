/*******************************************************************************
 *   
 *   Copyright 2010 Mytech Ingenieria Aplicada <http://www.mytechia.com>, Alejandro Paz, Gervasio Varela
 *   Victor Sonora
 * 
 *   This file is part of Mytechia Commons.
 *
 *   Mytechia Commons is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Mytechia Commons is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Mytechia Commons.  If not, see <http://www.gnu.org/licenses/>.
 * 
 ******************************************************************************/

package com.mytechia.commons.framework.simplemessageprotocol;

import com.mytechia.commons.framework.simplemessageprotocol.exception.MessageFormatException;
import com.mytechia.commons.util.conversion.EndianConversor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/** 
 *
 * @author  Alex
 * @version 1
 *
 * File: Command.java
 * Date: 22/02/2008
 * Changelog:
 *      23/02/2016  --  victor - Refactor: merge with Message
 *      26/01/2010  --  gervasio - Change in the size of the header
 *      22/02/2008  --  Initial version
 */
public abstract class Command
{

    public static final byte INIT_BYTE = 0x45;
    public static final int HEADER_CHECKSUM_SIZE = 1;
    public static final int DATA_CHECKSUM_SIZE = 1;

    public static final int MAX_MESSAGE_SIZE = 2500; //bytes
    
    
    public static final int COMMAND_HEADER_SIZE = 8;
   
    public static final int INIT_BYTE_INDEX = 0;
    public static final int COMMAND_TYPE_INDEX = 1;
    public static final int SEQUENCE_NUMBER_INDEX = 2;
    public static final int ERROR_CODE_INDEX = 4;
    /** Index of Data Size field. Field of 2 bytes. */    
    public static final int DATA_SIZE_INDEX = 5;
    public static final int HEADER_CHECKSUM_INDEX = 7;
    public static final int DATA_INDEX = COMMAND_HEADER_SIZE;


    private byte commandType = 0;
    private int sequenceNumber = 0;
    private byte headerChecksum = 0;
    private byte[] data = null;
    private byte dataChecksum = 0;

    private byte errorCode;
    private int dataSize;

    private Endianness endianness = Endianness.LITTLE_ENDIAN;

    private MessageCoder messageCoder = null;

    private MessageDecoder messageDecoder = null;


    protected int decodingIndex;

    
    public Command() {
        this.messageCoder = new MessageCoder(this.endianness);
    }


    public Command(Endianness endianness) {
        this.endianness = endianness;
    }


    public Command(byte [] message) throws MessageFormatException
    {
        this(Endianness.LITTLE_ENDIAN, message);
    }


    public Command(Endianness endianness, byte [] message) throws MessageFormatException
    {
        this(endianness);
        this.messageDecoder = new MessageDecoder(this.endianness, message, DATA_INDEX);
        this.decodeMessage(message);
    }


    protected MessageCoder getMessageCoder() {
        this.messageCoder = new MessageCoder(this.endianness);
        return this.messageCoder;
    }


    protected MessageDecoder getMessageDecoder() {
        return this.messageDecoder;
    }


    public List<MessageFieldInfo> getCodingMessageInfo() {
        if (null != this.messageCoder) {
            return this.messageCoder.getMessageFieldInfo();
        }
        return new ArrayList<>();
    }


    public List<MessageFieldInfo> getDecodingMessageInfo() {
        if (null != this.messageDecoder) {
            return this.messageDecoder.getMessageFieldInfo();
        }
        return new ArrayList<>();
    }


    public byte getCommandType() {
        return commandType;
    }


    public int getSequenceNumber() {
        return sequenceNumber;
    }



    /**
     * Obtain data field size (include 'user data' and 'data checksum')
     * @return
     */
    public int getDataFieldSize() {
        int dataSize = getDataSize();

        if (dataSize > 0) {
            return dataSize + DATA_CHECKSUM_SIZE;
        }
        else {
            return 0;
        }
    }

    public byte getHeaderChecksum() {
        return headerChecksum;
    }

    public byte[] getData() {
        return data;
    }

    public byte getDataChecksum() {
        return dataChecksum;
    }

    protected void setCommandType(byte commandType) {
        this.commandType = commandType;
    }

    protected void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    protected void setHeaderChecksum(byte headerChecksum) {
        this.headerChecksum = headerChecksum;
    }

    protected void setDataChecksum(byte dataChecksum) {
        this.dataChecksum = dataChecksum;
    }




    /**
     * Calculate checksum byte for 'length' bytes beginning at 'initIndex'.
     *
     * @param data Array of data.
     * @param initIndex Array index for first byte.
     * @param length Length of data for that calculate checksum.
     * @return Calculated checksum byte.
     */
    protected byte calcChecksum(byte[] data, int initIndex, int length) {
        byte check = 0;

        for (int i = 0; i < length; i++) {
            check ^= data[initIndex + i];
        }

        return check;
    }



    protected int readString(StringBuilder string, byte[] data, int offset) throws MessageFormatException
    {
        return readStringFromBytes(string, data, offset);
    }

    public static int readStringFromBytes(StringBuilder string, byte[] data, int offset) throws MessageFormatException
    {

        if(data.length<=offset){
            throw new  MessageFormatException("Invalid data size");
        }

        int localOffset = 0;
        int idLen = EndianConversor.byteArrayLittleEndianToShort(data, offset);
        localOffset += EndianConversor.SHORT_SIZE_BYTES;
        string.replace(0, idLen, new String(data, offset+localOffset, idLen));
        string.setLength(idLen);
        localOffset += idLen;
        return localOffset;
    }


    protected void writeString(ByteArrayOutputStream dataStream, String string) throws IOException
    {
        writeStringInStream(dataStream, string);
    }

    public static int writeStringInStream(ByteArrayOutputStream dataStream, String string) throws IOException
    {
        byte[] lenData = new byte[2];
        byte[] idData = string.getBytes();
        EndianConversor.shortToLittleEndian((short) idData.length, lenData, 0);
        dataStream.write(lenData);
        dataStream.write(idData);
        return lenData.length + idData.length;
    }


    public void setData(byte[] data) {
        this.data = data;
        this.dataSize = data.length;
    }



    /**
     * Obtain user data size (data without checksum).
     * @return
     */
    public int getDataSize() {
        return (getData() == null) ? this.dataSize : getData().length;        
    }



    public void setErrorCode(byte errorCode)
    {
        this.errorCode = errorCode;
    }


    public byte getErrorCode()
    {
        return this.errorCode;
    }
   
    
    
    /**
     * 
     * @return
     */
    public byte[] codeMessage() throws MessageFormatException
    {

        setData(codeMessageData());

        byte[] bytes = new byte[COMMAND_HEADER_SIZE + getDataFieldSize()];
        
        // Init byte
        bytes[INIT_BYTE_INDEX] = INIT_BYTE;
        // Command type
        bytes[COMMAND_TYPE_INDEX] = getCommandType();
        // Secuence number   

        EndianConversor.ushortToLittleEndian(getSequenceNumber(), bytes, SEQUENCE_NUMBER_INDEX);
        
        int sequenceNumber = EndianConversor.byteArrayLittleEndianToUShort(bytes, SEQUENCE_NUMBER_INDEX);
        
        
        
        //error code
        bytes[ERROR_CODE_INDEX] = getErrorCode();


        // Data size (2 bytes)        
        EndianConversor.ushortToLittleEndian(getDataSize(), bytes, DATA_SIZE_INDEX);
        
        // Message data        
        if (getDataSize() > 0) {            
            System.arraycopy(getData(), 0, bytes, COMMAND_HEADER_SIZE, getDataSize());
            // Data checksum byte
            setDataChecksum(calcChecksum(bytes, COMMAND_HEADER_SIZE, getDataSize()));
            bytes[COMMAND_HEADER_SIZE + getDataSize()] = getDataChecksum();            
        }
        
        // Head checksum byte
        setHeaderChecksum(calcChecksum(bytes, 0, COMMAND_HEADER_SIZE - HEADER_CHECKSUM_SIZE));
        bytes[HEADER_CHECKSUM_INDEX] = getHeaderChecksum();

        return bytes;
    }


    protected byte[] codeMessageData() throws MessageFormatException
    {

        return new byte[0];

    }


    /**
     * 
     * @param messageHeaderData
     * @throws MessageFormatException
     */
    protected void decodeMessageHead(byte[] messageHeaderData) throws MessageFormatException {

        if (messageHeaderData == null) {
            throw new MessageFormatException("Null data.");
        }
        if (messageHeaderData.length < COMMAND_HEADER_SIZE) {
            throw new MessageFormatException("Invalid message size.");
        }
        if (messageHeaderData[INIT_BYTE_INDEX] != INIT_BYTE) {
            throw new MessageFormatException("Checksum error.");
        }
        
        // Calculate and verify head checksum
        byte headChecksum = calcChecksum(messageHeaderData, 0, COMMAND_HEADER_SIZE - HEADER_CHECKSUM_SIZE);
        if (messageHeaderData[HEADER_CHECKSUM_INDEX] != headChecksum) {
            throw new MessageFormatException("Head checksum error.");
        }

        // Obtain data size value
        int dataSizeValue = EndianConversor.byteArrayLittleEndianToUShort(messageHeaderData, DATA_SIZE_INDEX);
        if (dataSizeValue >= 0) {
            this.dataSize = dataSizeValue;
        }
        else {
            throw new MessageFormatException("Invalid data size value: " + dataSizeValue);
        }
        
        // Command type
        setCommandType(messageHeaderData[COMMAND_TYPE_INDEX]);
        // HeaderReply type
        this.errorCode = messageHeaderData[ERROR_CODE_INDEX];
        // Secuence number
        int sequenceNumber = EndianConversor.byteArrayLittleEndianToUShort(messageHeaderData, SEQUENCE_NUMBER_INDEX);
        if (sequenceNumber >= 0) {
            setSequenceNumber(sequenceNumber);
        }
        else {
            throw new MessageFormatException("Invalid sequence number value: " + sequenceNumber);
        }        
        // Head checksum
        setHeaderChecksum(headChecksum);
        // Data checksum
        //setDataChecksum((byte) 0);
        // Data
        //setData(new byte[0]);
    }


    /**
     * Decode data field of the message.
     * This method needs that first decode the head field of the message.
     *
     * @param bytes
     * @param initIndex Indicates index where dataField begins.
     * @throws MessageFormatException
     */
    protected void decodeMessageDataChecksum(byte[] bytes, int initIndex) throws MessageFormatException
    {

        int dataLen = getDataSize();

        if ((dataLen > 0) && (bytes == null)) {
            throw new MessageFormatException("Invalid message size.");
        }
        else if (bytes != null && (dataLen > bytes.length)) {
            throw new MessageFormatException("Invalid message size.");
        }

        if (dataLen == 0) {
            setData(new byte[0]);
            setDataChecksum((byte) 0);
        }
        else {
            // Calculate and verify data checksum (last data field byte)
            byte dataChecksum = calcChecksum(bytes, initIndex, dataLen);
            if (bytes[initIndex + dataLen] != dataChecksum) {
                throw new MessageFormatException("Data checksum error.");
            }

            setData(Arrays.copyOfRange(bytes, initIndex, initIndex + dataLen));
            setDataChecksum(dataChecksum);
        }

    }


    protected abstract int decodeMessageData(byte[] bytes, int initIndex) throws MessageFormatException;


    public final void decodeMessage(byte [] message) throws MessageFormatException
    {

        this.decodeMessageHead(message); //decode the header
        this.decodeMessageDataChecksum(message, DATA_INDEX); //check the data checksum
        this.decodeMessageData(message, DATA_INDEX); //decode the message

    }


    
    /**
     *
     * @param msgData
     * @return
     * @throws MessageFormatException
     */
    public static byte getMessageType(byte[] msgData) throws MessageFormatException
    {
        if (msgData.length >= COMMAND_HEADER_SIZE) {
            return msgData[COMMAND_TYPE_INDEX];
        }
        else {
            throw new MessageFormatException("Message header is too short.");
        }
    }


    /**
     *
     * @param msgData
     * @return
     * @throws MessageFormatException
     */
    public static byte getMessageErrorCode(byte[] msgData) throws MessageFormatException
    {
        if (msgData.length >= COMMAND_HEADER_SIZE) {
            return msgData[ERROR_CODE_INDEX];
        }
        else {
            throw new MessageFormatException("Message header is too short.");
        }
    }

}
