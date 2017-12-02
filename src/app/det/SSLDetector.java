package app.det;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SSLDetector {
    public HashSet<Map<String,String>> SSLList = new HashSet<>();
    ReadWriteLock ssllock = new ReentrantReadWriteLock();

    public SSLDetector(ReadWriteLock ilock) {
        ssllock = ilock;
    }

    public void addToSSLTable(StreamModel inputJSON) {
        HashMap<String,String> addToList = new HashMap<>();
        addToList.put("address",inputJSON.dest_address);
        addToList.put("domain",inputJSON.ssl.get("server_name"));
        ssllock.writeLock().lock();
        SSLList.add(addToList);
        ssllock.writeLock().unlock();
        /*System.out.println("Start of list.");
        for (ArrayList<String> temp : SSLList) {
            System.out.println(temp.get(0) + " " + temp.get(1));
        }
        System.out.println("End of list.");*/
    }
}
