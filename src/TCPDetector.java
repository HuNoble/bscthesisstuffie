import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TCPDetector {

    public HashSet<Map<String,Object>> TCPList = new HashSet<>();
    public long idCounter = 1;
    ReadWriteLock tcplock = new ReentrantReadWriteLock();

    public TCPDetector(ReadWriteLock ilock) {
        tcplock = ilock;
    }

    public void determineState(StreamModel inputJSON) {
        Map<String,Object> tuples = new HashMap<>();
        if (inputJSON.source_address.contains("192.168.")) { //TODO the other private ranges
            tuples.put("a_address",inputJSON.source_address);
            tuples.put("b_address",inputJSON.dest_address);
            tuples.put("a_port",inputJSON.source_port);
            tuples.put("b_port",inputJSON.dest_port);
        } else {
            tuples.put("b_address",inputJSON.source_address);
            tuples.put("a_address",inputJSON.dest_address);
            tuples.put("b_port",inputJSON.source_port);
            tuples.put("a_port",inputJSON.dest_port);
        }
        boolean seen = false;
        Map<String,Object> oldEntry = new HashMap<>();
        tcplock.readLock().lock();
        for (Map<String,Object> temp : TCPList) {
            if(temp.entrySet().containsAll(tuples.entrySet())) {
                seen = true;
                oldEntry.putAll(temp);
                break;
            }
        }
        tcplock.readLock().unlock();
        if(!(seen)) {
            addToTCPTable(inputJSON, tuples);
        } else {
            updateTCPTable(inputJSON, oldEntry);
        }
        //printOut();
    }

    public void addToTCPTable(StreamModel inputJSON, Map<String,Object> tuples) {
        if (Objects.equals(inputJSON.tcp.control_bits.get("syn"), "true")) {
            tuples.put("flow_ID",idCounter);
            idCounter++;
            tuples.put("state","three-way");
            tuples.put("packets_captured",1);
            if(tuples.get("a_address").equals(inputJSON.source_address)) {
                tuples.put("data_a_to_b",inputJSON.tcp.data_in_bytes);
                tuples.put("data_b_to_a",0);
            } else {
                tuples.put("data_b_to_a",inputJSON.tcp.data_in_bytes);
                tuples.put("data_a_to_b",0);
            }
            tuples.put("time_created",new Timestamp(System.currentTimeMillis()));
            tuples.put("time_last_seen",new Timestamp(System.currentTimeMillis()));
            if(!(inputJSON.tcp.http.get("header").equals("")))
                tuples.put("http_cache",inputJSON.tcp.http.get("header"));
            tuples.put("fin",0);
            tcplock.writeLock().lock();
            TCPList.add(tuples);
            tcplock.writeLock().unlock();
        }
    }

    public void updateTCPTable(StreamModel inputJSON, Map<String,Object> oldEntry) {
        tcplock.writeLock().lock();
        TCPList.remove(oldEntry);
        if (Objects.equals(inputJSON.tcp.control_bits.get("syn"), "true"))
            oldEntry.replace("state","established");
        if (Objects.equals(inputJSON.tcp.control_bits.get("fin"), "true")) {
            oldEntry.replace("fin",(int)oldEntry.get("fin")+1);
            if ((int)oldEntry.get("fin") == 2)
                oldEntry.replace("state","closed");
        }
        oldEntry.replace("packets_captured",(int)oldEntry.get("packets_captured") + 1);
        if(oldEntry.get("a_address").equals(inputJSON.source_address)) {
            oldEntry.replace("data_a_to_b",(int)oldEntry.get("data_a_to_b") + inputJSON.tcp.data_in_bytes);
        } else {
            oldEntry.replace("data_b_to_a",(int)oldEntry.get("data_b_to_a") + inputJSON.tcp.data_in_bytes);
        }
        oldEntry.replace("time_last_seen",new Timestamp(System.currentTimeMillis()));
        if(!(inputJSON.tcp.http.get("header").equals("")))
            //System.out.println("HTTP cahce is available.");
            if(oldEntry.containsKey("http_cache"))
                oldEntry.replace("http_cache",oldEntry.get("http_cache") + "\n" + inputJSON.tcp.http.get("header"));
            else
                oldEntry.put("http_cache",inputJSON.tcp.http.get("header"));
        TCPList.add(oldEntry);
        tcplock.writeLock().unlock();
    }

    public void garbageCollector() {
        List<Map<String,Object>> markedForDelete = new ArrayList<>();
        tcplock.readLock().lock();
        for (Map<String,Object> temp : TCPList) {
            Timestamp last_seen = (Timestamp)temp.get("time_last_seen");
            if(System.currentTimeMillis() > last_seen.getTime()+1000*30 && (int)temp.get("fin") == 2) {
                markedForDelete.add(temp);
            }
            else if(System.currentTimeMillis() > last_seen.getTime()+1000*120 && (int)temp.get("fin") == 1) {
                markedForDelete.add(temp);
            }
            else if(System.currentTimeMillis() > last_seen.getTime()+1000*601 && (int)temp.get("fin") == 0) {
                markedForDelete.add(temp);
            }
        }
        tcplock.readLock().unlock();
        tcplock.writeLock().lock();
        TCPList.removeAll(markedForDelete);
        tcplock.writeLock().unlock();
    }

    public void printOut() {
        System.out.println("begin");
        tcplock.readLock().lock();
        for (Map<String,Object> temp : TCPList) {
            System.out.println(temp);
        }
        tcplock.readLock().unlock();
        System.out.println("end");
    }
}
