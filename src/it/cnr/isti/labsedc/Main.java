package it.cnr.isti.labsedc;

import it.cnr.isti.labsedc.Objects.Process;
import org.w3c.dom.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Process> processes;

        if (args.length != 1) {
            System.err.println("Wrong args number");
            System.exit(1);
        }

        Document document = BPMNParser.parseXML(args[0]);

        if (document == null) {
            System.err.println("Error parsing xml");
            System.exit(1);
        }

        processes = BPMNParser.parseProcessesList(document);
        for (Process process : processes) System.out.print(process);

    }

}
