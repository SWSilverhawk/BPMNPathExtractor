package it.cnr.isti.labsedc.Objects.FlowObjects;

import it.cnr.isti.labsedc.Objects.BPMNObject;

import java.util.ArrayList;

public abstract class FlowObject implements BPMNObject {

    protected final String id;
    protected final String name;
    private ArrayList<String> incomingConnections;
    private ArrayList<String> outgoingConnections;

    public FlowObject(String id, String name) {
        this.id = id;
        this.name = name;
        incomingConnections = new ArrayList<>();
        outgoingConnections = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Incoming Connections: ").append(System.lineSeparator());
        for (String incomingConnection : incomingConnections) {
            stringBuilder.append(incomingConnection).append(System.lineSeparator());
        }
        stringBuilder.append("Outgoing Connections: ").append(System.lineSeparator());
        for (String outgoingConnection : outgoingConnections) {
            stringBuilder.append(outgoingConnection).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    public void addIncomingConnection(String connectionID) { incomingConnections.add(connectionID); }
    public void addOutgoingConnection(String connectionID) { outgoingConnections.add(connectionID); }

    public String getId() { return id; }
    public String getName() { return name; }

}
