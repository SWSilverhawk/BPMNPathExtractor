package it.cnr.isti.labsedc.Objects.FlowObjects.Gateways;

import it.cnr.isti.labsedc.Objects.FlowObjects.FlowObject;

public class Gateway extends FlowObject {

    private final GatewayType gatewayType;

    public Gateway(String id, String name, GatewayType gatewayType) {
        super(id, name);
        this.gatewayType = gatewayType;
    }

    @Override
    public String toString() {
        return "Id: " + id + System.lineSeparator() +
                "Name: " + name + System.lineSeparator() +
                "Type: " + gatewayType + System.lineSeparator() +
                super.toString();
    }

    public GatewayType getGatewayType() { return gatewayType; }
}
