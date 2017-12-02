package app.det;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {

    static ReadWriteLock ssllock = new ReentrantReadWriteLock();
    static ReadWriteLock dnslock = new ReentrantReadWriteLock();
    static ReadWriteLock tcplock = new ReentrantReadWriteLock();
    static SSLDetector ssldetector = new SSLDetector(ssllock);
    static DNSDetector dnsdetector = new DNSDetector(dnslock);
    static TCPDetector tcpdetector = new TCPDetector(tcplock);
    
    public static void main(String[] args){
        FlowDistributor flowDistributor = new FlowDistributor(ssldetector, dnsdetector, tcpdetector);
        flowDistributor.start();
        AppDetector apdet = new AppDetector(ssldetector, dnsdetector, tcpdetector);
        apdet.start();
    }
}
