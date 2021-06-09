package com.ensate.chatapp.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
* Encrypts or decrypts a file with IDEA.
* <p>
* The file format is compatible with that of IDEA V1.1 (IDEA_CMD.C, ETH version).
* The length of the plaintext data file is appended to the encrypted file in an 8 byte suffix.
*/
public class IdeaFileEncryption {

    private static final int     blockSize = 8;
    
    /**
    * Block cipher mode of operation.
    * <p>See <a href="https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation">Wikipedia</a>.
    * <p>The current implementation only supports ECB and CBC (with interleave factor 1).
    */
    public enum Mode {
       /** Electronic Codebook mode. (<a href="https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Electronic_Codebook_.28ECB.29">Wikipedia</a>) */
       ECB,
       /** Cipher Block Chaining mode. (<a href="https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Block_Chaining_.28CBC.29">Wikipedia</a>) */
       CBC};
    
    /**
    * Encrypts or decrypts a file.
    *
    * @param inputFileName
    *    Name of the input file.
    * @param outputFileName
    *    Name of the output file.
    * @param charKey
    *    The encryption key. A string of ASCII characters within the range 0x21 .. 0x7E.
    * @param encrypt
    *    true to encrypt, false to decrypt.
    * @param mode
    *    Mode of operation.
    */
    public static byte[] cryptString (String data, String charKey, boolean encrypt) {

            Idea idea = new Idea(charKey, encrypt);
            BlockStreamCrypter bsc = new BlockStreamCrypter(idea, encrypt, Mode.CBC);
            long inDataLen;
            long outDataLen;

            if (encrypt) {
                inDataLen = data.length();
                outDataLen = (inDataLen + blockSize - 1) / blockSize * blockSize;
            } else {
                inDataLen = data.length() - blockSize;
                outDataLen = inDataLen;
            }
            
            pumpData (data, inDataLen, outDataLen, bsc);

            if (encrypt) {
                byte[] a = packDataLength(inDataLen);
                bsc.crypt(a, 0);
                return a;
            } 
            
            ByteBuffer buf = ByteBuffer.allocate(blockSize);
            byte[] a = buf.array();
            bsc.crypt(a, 0);
            return a;
    }

    public static void pumpData (String data, long inDataLen, long outDataLen, BlockStreamCrypter bsc) {
        final int bufSize = 0x200000;
        ByteBuffer buf = ByteBuffer.allocate(bufSize);
        long filePos = 0;
        while (filePos < inDataLen) {
            int reqLen = (int)Math.min(inDataLen - filePos, bufSize);     
            buf.position(0);
            buf.limit(reqLen);
            int chunkLen = (reqLen + blockSize - 1) / blockSize * blockSize;
            Arrays.fill(buf.array(), reqLen, chunkLen, (byte)0);
            for (int pos = 0; pos < chunkLen; pos += blockSize) {
                bsc.crypt(buf.array(), pos); 
            }
            reqLen = (int)Math.min(outDataLen - filePos, chunkLen);
            buf.position(0);
            buf.limit(reqLen);
            filePos += chunkLen;
        }

    }
    
    private static class BlockStreamCrypter {
       Idea            idea;
       boolean         encrypt;
       Mode            mode;
       byte[]          prev;                         // data of the previous ciphertext block
       byte[]          newPrev;
       BlockStreamCrypter (Idea idea, boolean encrypt, Mode mode) {
          this.idea = idea;
          this.encrypt = encrypt;
          this.mode = mode;
          prev = new byte[blockSize];
          newPrev = new byte[blockSize]; }
       void crypt (byte[] data, int pos) {
          switch (mode) {
             case ECB: {
                idea.crypt(data, pos);
                break; }
             case CBC: {
                if (encrypt) {
                   xor(data, pos, prev);
                   idea.crypt(data, pos);
                   System.arraycopy(data, pos, prev, 0, blockSize); }
                 else {
                   System.arraycopy(data, pos, newPrev, 0, blockSize);
                   idea.crypt(data, pos);
                   xor(data, pos, prev);
                   byte[] temp = prev;
                   prev = newPrev;
                   newPrev = temp; }
                break; 
                       }
          }
       }
    }
    
    private static long readDataLength (FileChannel channel, BlockStreamCrypter bsc) throws IOException {
       ByteBuffer buf = ByteBuffer.allocate(blockSize);
       int trLen = channel.read(buf);
       if (trLen != blockSize) {
          throw new IOException("Unable to read data length suffix."); }
       byte[] a = buf.array();
       bsc.crypt(a, 0);
       return unpackDataLength(a); }
    
    private static void writeDataLength (FileChannel channel, long dataLength, BlockStreamCrypter bsc) throws IOException {
       byte[] a = packDataLength(dataLength);
       bsc.crypt(a, 0);
       ByteBuffer buf = ByteBuffer.wrap(a);
       int trLen = channel.write(buf);
       if (trLen != blockSize) {
          throw new IOException("Error while writing data length suffix."); 
       }
    }
    
    // Packs an integer into an 8-byte block. Used to encode the file size.
    // To support larger files, we allow 13 more bits than the original IDEA V1.1 implementation.
    // But files larger than 4GB are no longer backward compatible with the old IDEA V1.1 file structure.
    private static byte[] packDataLength (long i) {
       if (i > 0x1FFFFFFFFFFFL) {                    // 45 bits
          throw new IllegalArgumentException("File too long."); }
       byte[] b = new byte[blockSize];
       b[7] = (byte)(i <<  3);
       b[6] = (byte)(i >>  5);
       b[5] = (byte)(i >> 13);
       b[4] = (byte)(i >> 21);
       b[3] = (byte)(i >> 29);
       b[2] = (byte)(i >> 37);
       return b; }
    
    // Extracts an integer from an 8-byte block. Used to decode the file size.
    // Returns -1 if the encoded value is invalid. This means that the input file is not a valid cryptogram.
    private static long unpackDataLength (byte[] b) {
       if (b[0] != 0 || b[1] != 0 || (b[7] & 7) != 0) {
          return -1; }
       return
          (long)(b[7] & 0xFF) >>  3 |
          (long)(b[6] & 0xFF) <<  5 |
          (long)(b[5] & 0xFF) << 13 |
          (long)(b[4] & 0xFF) << 21 |
          (long)(b[3] & 0xFF) << 29 |
          (long)(b[2] & 0xFF) << 37; }
    
    private static void xor (byte[] a, int pos, byte[] b) {
       for (int p = 0; p < blockSize; p++) {
          a[pos + p] ^= b[p]; }}
    
}
