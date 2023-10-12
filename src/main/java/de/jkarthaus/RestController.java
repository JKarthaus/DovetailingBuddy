package de.jkarthaus;

import de.jkarthaus.model.dto.DovetailRequest;
import de.jkarthaus.model.dto.DovetailResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;

@Controller("/dovetail")
@RequiredArgsConstructor
public class RestController {

    @Post(uri = "/generateGCode", produces = MediaType.APPLICATION_JSON)
    public DovetailResponse buildDovetail(DovetailRequest dovetailRequest) {
        return new DovetailResponse("Ouside","Inside");
    }


}
