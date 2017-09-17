package it.cnr.isti.labsedc.Objects.Connections;

import it.cnr.isti.labsedc.Objects.BPMNObject;

public class Connection implements BPMNObject {

    protected final String id;
    protected final String name;
    private ConnectionType connectionType;
    private String sourceRef;
    private String targetRef;

    public Connection(String id, String name, String sourceRef, String targetRef, ConnectionType connectionType) {
        this.id = id;
        this.name = name;
        this.sourceRef = sourceRef;
        this.targetRef = targetRef;
        this.connectionType = connectionType;
    }

    @Override
    public String toString() {
        return "Id: " + id + System.lineSeparator() +
                "Name: " + name + System.lineSeparator() +
                "Type: " + connectionType + System.lineSeparator() +
                "Source Ref: " + sourceRef + System.lineSeparator() +
                "Target Ref: " + targetRef + System.lineSeparator();
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public ConnectionType getConnectionType() { return connectionType; }
    public String getSourceRef() { return sourceRef; }
    public String getTargetRef() { return targetRef; }
}
