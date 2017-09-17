package it.cnr.isti.labsedc.Objects;

import it.cnr.isti.labsedc.Objects.Connections.Connection;
import it.cnr.isti.labsedc.Objects.FlowObjects.FlowObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Process {

    private final String id;
    private final String name;
    private final boolean executable;
    private ArrayList<String> startEvents;
    private HashMap<String, FlowObject> flowObjects;
    private HashMap<String, Connection> connections;

    public Process(String id, String name, boolean executable) {
        this.id = id;
        this.name = name;
        this.executable = executable;
        startEvents = new ArrayList<>();
        flowObjects = new HashMap<>();
        connections = new HashMap<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return  false;
        if (getClass() != obj.getClass()) return false;
        Process process = (Process) obj;
        return this.id.equals(process.id);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id: ").append(id).append(System.lineSeparator()).
                append("Name: ").append(name).append(System.lineSeparator()).
                append("Is Executable: ").append(executable).append(System.lineSeparator()).
                append(System.lineSeparator());
        for (String key : flowObjects.keySet())
            stringBuilder.append(flowObjects.get(key).toString()).append(System.lineSeparator());
        for (String key : connections.keySet())
            stringBuilder.append(connections.get(key).toString()).append(System.lineSeparator());
        return stringBuilder.toString();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isExecutable() { return executable; }

    public ArrayList<String> getStartEvents() { return startEvents; }
    public HashMap<String, FlowObject> getFlowObjects() { return flowObjects; }
    public HashMap<String, Connection> getConnections() { return connections; }

    public void addStartEvent(String id) { startEvents.add(id); }
    public void addFlowObject(String key, FlowObject flowObject) { flowObjects.put(key, flowObject); }
    public void addConnection(String key, Connection connection) { connections.put(key, connection); }

}
