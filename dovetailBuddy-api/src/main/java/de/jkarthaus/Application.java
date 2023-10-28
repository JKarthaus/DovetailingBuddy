package de.jkarthaus;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
@OpenAPIDefinition(
        info = @Info(
                title = "DovetaillingBuddy",
                version = "1.0",
                description = "Generates Dovetailling CNC Code",
                license = @License(name = "Apache 2.0", url = " http://www.apache.org/licenses/"),
                contact = @Contact(url = "https://github.com/JKarthaus/DovetailingBuddy", name = "Joern Karthaus")
        )
)

public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
