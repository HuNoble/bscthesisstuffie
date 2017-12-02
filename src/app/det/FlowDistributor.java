package app.det;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlowDistributor implements Runnable {

    private Thread t;
    public static long lastRun = System.currentTimeMillis();
    SSLDetector ssldetector;
    DNSDetector dnsdetector;
    TCPDetector tcpdetector;
    private long first_file = 0;
    private long first_realtime = 0;

    public FlowDistributor(SSLDetector s, DNSDetector d, TCPDetector t){
        ssldetector = s;
        dnsdetector = d;
        tcpdetector = t;
    }

    @Override
    public void run() {
        try {
            //BufferedReader stdInput = new BufferedReader(new FileReader("/root/IdeaProjects/Nemo/out/production/Nemo/fifo1"));
            BufferedReader stdInput = new BufferedReader(new FileReader("emese2.txt"));
            String line = null;
            while (true) {
                if ((line = stdInput.readLine()) != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    StreamModel inputJSON = null;
                    try {
                        inputJSON = objectMapper.readValue(line, StreamModel.class);
                        //Tesztkod Mesinek
                        boolean waitForIt = true;
                        while (waitForIt) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date parsedDate = dateFormat.parse(inputJSON.timestamp);
                            long elapsed = parsedDate.getTime();
                            long now = System.currentTimeMillis();
                            if (first_file == 0)
                                first_file = elapsed;
                            if (first_realtime == 0)
                                first_realtime = now;
                            if (now-first_realtime > elapsed-first_file) {
                                waitForIt = false;
                            }
                        }
                        //Tesztkod vege
                        if (!(inputJSON.tcp.sequence_number.equals(""))) {
                            tcpdetector.determineState(inputJSON);
                            if (!(inputJSON.ssl.get("server_name").equals(""))) {
                                ssldetector.addToSSLTable(inputJSON);
                            }
                        } else {
                            dnsdetector.addToDNSTable(inputJSON);
                        }
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (System.currentTimeMillis() > lastRun + 1000 * 10) {
                    tcpdetector.printOut();
                    tcpdetector.garbageCollector();
                    lastRun = System.currentTimeMillis();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start ();
        }
    }
}
