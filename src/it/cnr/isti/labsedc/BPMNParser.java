package it.cnr.isti.labsedc;

import it.cnr.isti.labsedc.Objects.Connections.*;
import it.cnr.isti.labsedc.Objects.FlowObjects.Activities.*;
import it.cnr.isti.labsedc.Objects.FlowObjects.Events.*;
import it.cnr.isti.labsedc.Objects.FlowObjects.FlowObject;
import it.cnr.isti.labsedc.Objects.FlowObjects.Gateways.*;
import it.cnr.isti.labsedc.Objects.Process;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class BPMNParser {

    @Nullable
    public static Document parseXML(String path) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(path));
            document.getDocumentElement().normalize();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Process> parseProcessesList(Document document) {

        ArrayList<Process> processes = new ArrayList<>();
        NodeList processesNodes = document.getElementsByTagNameNS("http://www.omg.org/spec/BPMN/20100524/MODEL", "process");

        for (int i = 0; i < processesNodes.getLength(); i++) {

            Process process;
            Node processNode = processesNodes.item(i);
            String processID = getAttributeValue(processNode, "id");
            String processName = getAttributeValue(processNode, "name");
            String processExecutable = getAttributeValue(processNode, "isExecutable");

            process = new Process(processID, processName, Boolean.parseBoolean(processExecutable));

            NodeList processChildNodes = processNode.getChildNodes();
            for (int j = 0; j < processChildNodes.getLength(); j++) {
                Node childNode = processChildNodes.item(j);
                String nodeID = getAttributeValue(childNode, "id");
                String localName = childNode.getLocalName();
                if (localName == null) continue;
                switch (localName) {
                    case "startEvent":
                        process.addFlowObject(nodeID, parseEvent(childNode, EventType.START_EVENT));
                        process.addStartEvent(nodeID);
                        break;
                    case "intermediateThrowEvent":
                        process.addFlowObject(nodeID, parseEvent(childNode, EventType.INTERMEDIATE_THROW_EVENT));
                        break;
                    case "intermediateCatchEvent":
                        process.addFlowObject(nodeID, parseEvent(childNode, EventType.INTERMEDIATE_CATCH_EVENT));
                        break;
                    case "endEvent":
                        process.addFlowObject(nodeID, parseEvent(childNode, EventType.END_EVENT));
                        break;
                    case "task":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.TASK));
                        break;
                    case "sendTask":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.SEND_TASK));
                        break;
                    case "receiveTask":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.RECEIVE_TASK));
                        break;
                    case "userTask":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.USER_TASK));
                        break;
                    case "manualTask":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.MANUAL_TASK));
                        break;
                    case "businessRuleTask":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.BUSINESS_RULE_TASK));
                        break;
                    case "serviceTask":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.SERVICE_TASK));
                        break;
                    case "scriptTask":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.SCRIPT_TASK));
                        break;
                    case "callActivity":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.CALL_ACTIVITY));
                        break;
                    case "subProcess":
                        process.addFlowObject(nodeID, parseActivity(childNode, ActivityType.SUB_PROCESS));
                        break;
                    case "exclusiveGateway":
                        process.addFlowObject(nodeID, parseGateway(childNode, GatewayType.EXCLUSIVE_GATEWAY));
                        break;
                    case "inclusiveGateway":
                        process.addFlowObject(nodeID, parseGateway(childNode, GatewayType.INCLUSIVE_GATEWAY));
                        break;
                    case "complexGateway":
                        process.addFlowObject(nodeID, parseGateway(childNode, GatewayType.COMPLEX_GATEWAY));
                        break;
                    case "eventBasedGateway":
                        process.addFlowObject(nodeID, parseGateway(childNode, GatewayType.EVENT_BASED_GATEWAY));
                        break;
                    case "sequenceFlow":
                        process.addConnection(nodeID, parseConnection(childNode, ConnectionType.SEQUENCE_FLOW));
                        break;
                    case "messageFlow":
                        process.addConnection(nodeID, parseConnection(childNode, ConnectionType.MESSAGE_FLOW));
                        break;
                    case "association":
                        process.addConnection(nodeID, parseConnection(childNode, ConnectionType.ASSOCIATION));
                        break;
                }
            }

            processes.add(process);

        }

        return processes;

    }

    private static Event parseEvent(Node eventNode, EventType eventType) {

        Event event;
        String eventID = getAttributeValue(eventNode, "id");
        String eventName = getAttributeValue(eventNode, "name");

        switch (eventType) {
            case START_EVENT:
                event = new StartEvent(eventID, eventName, eventType);
                break;
            case END_EVENT:
                event = new EndEvent(eventID, eventName, eventType);
                break;
            default:
                event = new IntermediateEvent(eventID, eventName, eventType);
                break;
        }

        addConnectionsToObject(event, eventNode);

        return event;

    }

    private static Activity parseActivity(Node activityNode, ActivityType activityType) {

        Activity activity;
        String activityID = getAttributeValue(activityNode, "id");
        String activityName = getAttributeValue(activityNode, "name");

        switch (activityType) {
            case SUB_PROCESS:
                activity = new SubProcess(activityID, activityName, activityType);
                break;
            default:
                activity = new Task(activityID, activityName, activityType);
                break;
        }

        addConnectionsToObject(activity, activityNode);

        return activity;

    }

    private static Gateway parseGateway(Node gatewayNode, GatewayType gatewayType) {

        Gateway gateway;
        String gatewayID = getAttributeValue(gatewayNode, "id");
        String gatewayName = getAttributeValue(gatewayNode, "name");

        gateway = new Gateway(gatewayID, gatewayName, gatewayType);

        addConnectionsToObject(gateway, gatewayNode);

        return gateway;

    }

    private static Connection parseConnection(Node connectionNode, ConnectionType connectionType) {

        Connection connection;
        String sourceRef = getAttributeValue(connectionNode, "sourceRef");
        String targetRef = getAttributeValue(connectionNode, "targetRef");
        String connectionID = getAttributeValue(connectionNode, "id");
        String connectionName = getAttributeValue(connectionNode, "name");

        connection = new Connection(connectionID, connectionName, sourceRef, targetRef, connectionType);

        return connection;

    }

    private static void addConnectionsToObject(FlowObject object, Node objectNode) {

        NodeList childNodes = objectNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node eventChildNode = childNodes.item(i);
            String localName = eventChildNode.getLocalName();
            if (localName == null) continue;
            switch (localName) {
                case "incoming":
                    object.addIncomingConnection(eventChildNode.getTextContent());
                    break;
                case "outgoing":
                    object.addOutgoingConnection(eventChildNode.getTextContent());
                    break;
            }
        }

    }

    @Nullable
    private static String getAttributeValue(Node node, String attributeName) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) return null;
        Node attributeNode = attributes.getNamedItem(attributeName);
        if (attributeNode == null) return null;
        return attributeNode.getNodeValue();
    }

}
