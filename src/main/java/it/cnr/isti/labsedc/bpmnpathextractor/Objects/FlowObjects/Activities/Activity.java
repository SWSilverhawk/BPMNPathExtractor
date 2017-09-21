package it.cnr.isti.labsedc.bpmnpathextractor.Objects.FlowObjects.Activities;

import it.cnr.isti.labsedc.bpmnpathextractor.Objects.FlowObjects.FlowObject;

public class Activity extends FlowObject {

    private final ActivityType activityType;

    public Activity(String id, String name, ActivityType activityType) {
        super(id, name);
        this.activityType = activityType;
    }

    @Override
    public String toString() {
        return "ID: " + id + System.lineSeparator() +
                "Name: " + name + System.lineSeparator() +
                "Type: " + activityType + System.lineSeparator() +
                super.toString();
    }

    public ActivityType getActivityType() { return activityType; }
}
