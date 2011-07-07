package com.nullprogram.chess.ai;

import java.io.OutputStream;
import java.io.InputStream;
import java.util.logging.Logger;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class HelperSocket {
    /** This class's Logger. */
    private static final Logger LOG = Logger.getLogger("ai.HelperSocket");

    private final Socket socket;
    private InputStream in;
    private OutputStream out;
    private ObjectInputStream oin;
    private ObjectOutputStream oout;

    public HelperSocket(Socket sock) {
        socket = sock;
        if (sock == null) {
            return;
        }
        try {
            in = sock.getInputStream();
            out = sock.getOutputStream();
        } catch (java.io.IOException e) {
            LOG.warning("failed to initilize helper");
        }
    }

    public ObjectInputStream getIn() throws java.io.IOException {
        if (oin == null) {
            oin = new ObjectInputStream(in);
        }
        return oin;
    }

    public ObjectOutputStream getOut() throws java.io.IOException {
        if (oout == null) {
            oout = new ObjectOutputStream(out);
        }
        return oout;
    }
}
