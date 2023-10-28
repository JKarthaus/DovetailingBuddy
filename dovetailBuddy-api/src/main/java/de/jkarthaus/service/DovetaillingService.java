package de.jkarthaus.service;

import de.jkarthaus.config.MillingConfig;
import de.jkarthaus.enums.Axis;
import de.jkarthaus.enums.GCodes;
import de.jkarthaus.exception.BuildDovetailFailedException;
import de.jkarthaus.model.DoveTailModel;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class DovetaillingService {

    Double actX;
    Double actY;
    Double actZ;


    public DoveTailModel buildDovetail(
            Double toolDiameter,
            Double partWidth,
            Double partThickness,
            Integer count) throws BuildDovetailFailedException {
        actX = (double) 0;
        actY = (double) 0;
        actZ = (double) 0;
        if (toolDiameter <= 0) {
            throw new BuildDovetailFailedException("Tool Diameter must greater than 0");
        }
        if (partWidth <= (toolDiameter * 3)) {
            throw new BuildDovetailFailedException("part width to small");
        }
        if (partThickness <= 1) {
            throw new BuildDovetailFailedException("part is to thin");
        }
        if ((count * toolDiameter) >= partWidth) {
            throw new BuildDovetailFailedException("to many count for width");
        }
        return new DoveTailModel(
                getOuterDovetail(toolDiameter, partWidth, partThickness, count),
                getInnerDovetail(toolDiameter, partWidth, partThickness, count)
        );
    }


    private String getOuterDovetail(
            Double toolDiameter,
            Double partWidth,
            Double partThickness,
            Integer count
    ) throws BuildDovetailFailedException {
        if ((count * toolDiameter) > (partWidth - 10)) {
            throw new BuildDovetailFailedException("width to small for " + toolDiameter + " * " + count);
        }
        Double actY = (double) 0;
        log.info("build outer dovetail with:{} Pockets", count);
        String result = "(outer dovetail)\n";
        for (int i = 0; i < count; i++) {
            result += "(pocket )" + (i + 1) + " of " + count + ")\n";
            result += buildPocket(toolDiameter, partWidth, partThickness, actY);
        }
        return result;
    }

    private String getInnerDovetail(
            Double toolDiameter,
            Double partWidth,
            Double partThickness,
            Integer count
    ) throws BuildDovetailFailedException {
        if ((count * toolDiameter) > (partWidth - 10)) {
            throw new BuildDovetailFailedException("width to small for " + toolDiameter + " * " + count);
        }

        //TODO : implement
        return null;
    }


    private StringBuffer buildPocket(
            Double toolDiameter,
            Double width,
            Double deep,
            Double yStart) throws BuildDovetailFailedException {
        StringBuffer result = new StringBuffer("% Dovetail\n");
        if (width < toolDiameter) {
            log.error("dovetail with:{} smaller then toolDiameter:[}", width, toolDiameter);
            throw new BuildDovetailFailedException("dovertail smaller than toolDiameter");
        }
        boolean upToDown = true;
        // Clearance x
        result.append(GCodes.G1).append(" ")
                .append(Axis.X).append(MillingConfig.CLEARANCE * -1)
                .append(" ").append(Axis.Z).append(MillingConfig.CLEARANCE)
                .append("\n");
        // to Startposition
        result.append(GCodes.G0)
                .append(" ").append(Axis.Y).append((yStart + toolDiameter) * -1)
                .append("\n");
        // Pocket
        Integer ySteps = Double.valueOf((width - toolDiameter) / getOverlapYStep(toolDiameter)).intValue();
        Double yStepRest = width - toolDiameter - (ySteps * getOverlapYStep(toolDiameter));
        // ------------  1 Cut Full
        result.append(buildZStep(upToDown, deep, toolDiameter));
        upToDown = !upToDown;
        result.append(buildYStep(toolDiameter));
        // --- Overlap cuts
        for (int i = 0; i < ySteps; i++) {
            result.append(buildZStep(upToDown, deep, getOverlapYStep(toolDiameter)));
            upToDown = !upToDown;
            if (i < ySteps) {
                result.append(buildYStep(getOverlapYStep(toolDiameter)));
            }
        }
        // -- Rest Cut
        result.append(buildYStep(yStepRest));
        result.append(buildZStep(upToDown, deep, toolDiameter));
        upToDown = !upToDown;
        return result;
    }

    private StringBuffer buildYStep(Double stepWidth) {
        log.debug("build Y Step negativ direction");
        actY = actY - stepWidth;
        return new StringBuffer(GCodes.G1.toString()).append(" ")
                .append(Axis.Y).append(stepWidth * -1);

    }

    private StringBuffer buildZStep(boolean upToDown, Double deep, Double toolDiameter) {
        double downPos = (deep + toolDiameter + MillingConfig.CLEARANCE) * -1;
        double upPos = MillingConfig.CLEARANCE;
        if (upToDown) {
            log.debug("build Z Step from Up to Down");
            return new StringBuffer(GCodes.G1.toString()).append(" ")
                    .append(Axis.Z).append(downPos);
        } else {
            log.debug("build Z Step from down to up");
            return new StringBuffer(GCodes.G1.toString()).append(" ")
                    .append(Axis.Z).append(upPos);
        }
    }

    private Double getOverlapYStep(Double toolDiameter) {
        Double result = toolDiameter * MillingConfig.TOOL_OVERLAP_PERCENT / 100;
        log.debug("getYstep -Y toolDiameter={} overlap={}% = {}",
                toolDiameter,
                MillingConfig.TOOL_OVERLAP_PERCENT,
                result
        );
        return result;
    }

    private Integer getWithSteps(Double width, Double yStep) {
        Integer result = Double.valueOf(width / yStep).intValue();
        log.debug("getWithSteps -> with={} yStep={} = {}", width, yStep, result);
        return result;
    }

}
