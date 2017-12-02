package app.det;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DNSDetector {
    public HashSet<Map<String,String>> DNSList = new HashSet<>();
    ReadWriteLock dnslock = new ReentrantReadWriteLock();

    public DNSDetector(ReadWriteLock ilock) {
        dnslock = ilock;
    }

    public void addToDNSTable(StreamModel inputJSON) {
        dnslock.writeLock().lock();
        DNSList.addAll(Arrays.asList(inputJSON.dns));
        dnslock.writeLock().unlock();
        //System.out.println(DNSList);
    }
}
