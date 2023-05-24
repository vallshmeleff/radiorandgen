package com.example.radiostream;

import static java.util.Collections.binarySearch;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

//---------------------------------------------------
//
// RadioSTREAM Application
// We use an Internet radio stream to obtain the encryption (and decoding) key for the Vernam algorithm.
// We receive the digital data stream of any Internet radio at the same time at two points - the sender of the data (for encryption) and the recipient of the encrypted data.
// Write the data stream to the buffer. We are looking for a predetermined sequence of numbers in the buffer, which is known to the sender and recipient.
// After the given sequence is found, the next N numbers are read from the buffer. Where N= number of message characters to encrypt
// Because the sequence specified for waiting may fall on the buffer boundary (not completely fit in the buffer), we will accumulate the buffer data in a variable of a larger size (3-5)
// of the buffer size. Re-seek the sequence in the variable, shift the data by the size of the buffer, add a new buffer, and search again.
// Unlike asymmetric encryption (RSA, ECC), with this key derivation technology, the sender and recipient of information do not exchange public keys and encryption is faster.
// There are many opportunities to improve this technology.
//
// Read the Internet radio stream into the buffer and look for an array byte[] efind in the buffer
// The more bytes you specify to search, the longer you have to wait for a match. In this example - approximately 1-2 matches in 5 minutes
// Because the launch of the application for searching for keys in the radio stream is performed at the sender and receiver independently and not synchronously, for use in data
// encryption (commercial) it is necessary to create a storage of found random keys. It takes several attempts to decode a message with several keys.
//
//---------------------------------------------------
// (c) by Valery Shmelev
// https://www.linkedin.com/in/valery-shmelev-479206227/
// https://github.com/vallshmeleff
//---------------------------------------------------
public class MainActivity extends AppCompatActivity {
    // We will look for this sequence of bytes in the streaming data buffer
    public static byte[] efind = {0,1,5,3,7,3,6,12,4,3,7}; // We are looking for an array of bytes in the buffer. If found - write to Log
    // If this sequence is found, then we read as many bytes (random bytes) as needed to encrypt using the Vernam algorithm
    // The longer the byte[] efind array to wait, the longer it will wait in the Internet radio data stream
    public static byte[] buffercopy = new byte[1000]; // Public copy of buffer
    public static int k = 0; // Counter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReadStream(); // Reading an Internet radio stream into a buffer

    } // OnCreate

    protected void ReadStream() {
    byte[] b = new byte[1000]; // Buffer for data from the Internet stream
         byte[] ee = new byte[3000]; // Here we will copy the contents of three buffers to search for given numbers
        int ev = 0; // The variable ee fits 3 buffers b. This is a counter from 0 to 2 - what part of ee is filled from buffer b
        new Thread(new Runnable()  {
            @Override
            public void run() {
                try {
                    URL radioUrl = new URL("https://icecast-vgtrk.cdnvideo.com/vestifm_aac_64kbps");
                    InputStream in = radioUrl.openStream();
                    InputStream is = new BufferedInputStream(in);
                    is.read(b);
                    Log.i("== Stream =", " == == Stream DATA to Buffer == == ");

                    int count;
                    while ((count = is.read(b)) != -1) {
                        is.read(b);
                        buffercopy = b; // Copy buffer b to buffercopy

                        //= String doc = new String(b, "UTF-8"); // For Debug
                        //= Log.i("== Stream =", " == == Stream DATA == == " + doc); // Buffer to LOG Screen
                        FindDigit(); // We are looking for a given array of bytes in the buffer (array of bytes)
                    }
                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.getMessage();
                }
                //return null;
            }

            }).start(); // Thread


    } // ReadStream

    public void FindDigit() { // We are looking for a given sequence of digits in the buffer. If found - write to Log
        int eh = searchBytePattern(buffercopy,efind); // Find efind in buffercopy
    } // FindDigit

    public static int searchBytePattern(byte[] earray, byte[] epattern) {
        int epr = earray.length;
        int eptt = epattern.length;
        int i = 0;

        if (epattern.length > earray.length) {
            return -1;
        }
        int j = 0; // Counter in epattern
        // int lg = 0; // Not find
        for (i = 0; i <= epr - eptt; i++) { // От начала до конца буфера
            if (earray[i] == epattern[j]) {

                if (j < eptt-1){
                    j++;

                } else { // Если в потоке встретилась заданная последовательность
                    k++;
                    Log.i("== Stream FIND =", " == == Given array efind found in data stream == == !!!!!!!!!!!!!!!!!!" + String.valueOf(k));
                }

            }


        }
        return -1;
    }





} // MainActivity
