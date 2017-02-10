package com.genymobile.gnirehtet;

import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RelayTunnel implements Tunnel {

    private static final String TAG = RelayTunnel.class.getName();

    private final RelayClient client;

    public RelayTunnel(RelayClient client) {
        this.client = client;
    }

    @Override
    public void open() throws IOException {
        client.connect();
    }

    @Override
    public void waitForOpened() throws InterruptedException {
        client.waitForConnected();
    }

    @Override
    public void send(byte[] packet, int len) throws IOException {
        if (GnirehtetService.VERBOSE) {
            Log.d(TAG, "Sending..." + Binary.toString(packet, len));
        }
        ByteBuffer buffer = ByteBuffer.wrap(packet, 0, len);
        while (buffer.hasRemaining()) {
            client.getChannel().write(buffer);
        }
    }

    @Override
    public int receive(byte[] packet) throws IOException {
        int r = client.getChannel().read(ByteBuffer.wrap(packet));
        if (GnirehtetService.VERBOSE) {
            Log.d(TAG, "Receiving..." + Binary.toString(packet, r));
        }
        return r;
    }
}
