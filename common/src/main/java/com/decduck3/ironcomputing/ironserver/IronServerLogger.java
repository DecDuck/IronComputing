package com.decduck3.ironcomputing.ironserver;

import com.decduck3.ironcomputing.IronComputing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IronServerLogger extends Thread {
    InputStream is;
    boolean error;

    // reads everything from is until empty.
    IronServerLogger(InputStream is, boolean error) {
        this.is = is;
        this.error = error;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (this.error) {
                    IronComputing.LOGGER.error("[ironserver] {}", line);
                } else {
                    IronComputing.LOGGER.info("[ironserver] {}", line);
                }
            }
        } catch (IOException ioe) {
            IronComputing.LOGGER.error(ioe.getMessage());
        }
    }
}