package it.cnr.isti.labsedc.bpmnpathextractor;

import it.cnr.isti.labsedc.bpmnpathextractor.Objects.BPMNProcess;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("unchecked")
public class ExtractionsLauncher {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        ExtractionManager extractionManagerTask;
        ServerSocket serverSocket;
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;
        Future<?> taskResult;

        try {
            serverSocket = new ServerSocket(13500);  

        while (true) {
                Socket clientChannel;
                clientChannel = serverSocket.accept();
                List<String> poolsID = new ArrayList<>();
                List<String> lanesID = new ArrayList<>();
                ArrayList<BPMNProcess> processes;
                outputStream = new ObjectOutputStream(clientChannel.getOutputStream());
                inputStream = new ObjectInputStream(clientChannel.getInputStream());

                String bpmnPath = (String) inputStream.readObject();
                int deepness = (int) inputStream.readObject();

                int size = (int) inputStream.readObject();
                for (int i = 0; i < size; i++)
                    poolsID.add((String) inputStream.readObject());

                size = (Integer) inputStream.readObject();
                for (int i = 0; i < size; i++)
                    lanesID.add((String) inputStream.readObject());

                int pathType = (int) inputStream.readObject();

                extractionManagerTask = new ExtractionManager(bpmnPath, deepness, poolsID, lanesID, pathType);
                taskResult = executorService.submit(extractionManagerTask);
                processes = (ArrayList<BPMNProcess>) taskResult.get();

                processes.removeIf(process -> process.getDeepness() != 0);

                outputStream.writeObject(processes.size());
                for (BPMNProcess process : processes) {
                    if (process.getDeepness() == 0) {
                        outputStream.writeObject(process.getId());
                        outputStream.writeObject(process.getPaths());
                    }
                }

                outputStream.flush();
        	}
        } catch (InterruptedException | ExecutionException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}