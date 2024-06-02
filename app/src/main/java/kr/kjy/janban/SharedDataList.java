package kr.kjy.janban;

import java.util.ArrayList;
import java.util.List;

public class SharedDataList {
    private List<byte[]> receivedDataList = new ArrayList<>();

    private static final SharedDataList instance = new SharedDataList();

    public static SharedDataList getInstance() {
        return instance;
    }

    public synchronized void addReceivedData(byte[] data) {
        receivedDataList.add(data);
    }

    public synchronized List<byte[]> getReceivedDataList() {
        return receivedDataList;
    }
}