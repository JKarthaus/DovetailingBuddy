package de.jkarthaus;

import de.jkarthaus.exception.BuildDovetailFailedException;
import de.jkarthaus.model.DoveTailModel;
import de.jkarthaus.model.dto.DovetailRequest;
import de.jkarthaus.model.dto.DovetailResponse;
import de.jkarthaus.service.DovetaillingService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.exceptions.HttpClientException;
import lombok.RequiredArgsConstructor;

@Controller("/dovetail")
@RequiredArgsConstructor
public class RestController {

    final DovetaillingService dovetaillingService;

    @Post(uri = "/generateGCode", produces = MediaType.APPLICATION_JSON)
    public DovetailResponse buildDovetail(DovetailRequest dovetailRequest) {
        DoveTailModel doveTailModel;
        try {
            doveTailModel = dovetaillingService.buildDovetail(
                    dovetailRequest.getToolDiameter(),
                    dovetailRequest.getWidth(),
                    dovetailRequest.getThickness(),
                    dovetailRequest.getCount()
            );
        } catch (BuildDovetailFailedException e) {
            throw new HttpClientException(e.getMessage());
        }
        return new DovetailResponse(
                doveTailModel.getOuterDovetail(),
                doveTailModel.getInnerDovetail()
        );
    }


}
