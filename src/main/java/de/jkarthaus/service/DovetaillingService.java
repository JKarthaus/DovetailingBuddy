package de.jkarthaus.service;

import de.jkarthaus.config.MillingConfig;
import de.jkarthaus.enums.Axis;
import de.jkarthaus.enums.GCodes;
import jakarta.inject.Singleton;

@Singleton
public class DovetaillingService {

    public String getOuterDovetail() {
        //TODO : implement
        return null;
    }

    public String getInnerDovetail() {
        //TODO : implement
        return null;
    }


    private StringBuffer buildPocket(
            Double toolDiameter,
            Double width,
            Double deep,
            Double yStart) {
        StringBuffer result = new StringBuffer("% Dovetail\n");
        // Clearance x
        result.append(GCodes.G1)
                .append(" ").append(Axis.X).append(MillingConfig.CLEARANCE * -1)
                .append(" ").append(Axis.Z).append(MillingConfig.CLEARANCE)
                .append("\n");
        // to Startposition
        result.append(GCodes.G0)
                .append(" ").append(Axis.Y).append(yStart * -1)
                .append("\n");

        return result;
    }

}
