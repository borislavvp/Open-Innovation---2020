package com.kigalimedicine;

import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title="Kigali API",
                version = "1.0.0"
        ),
        servers =  {
                @Server(url = "https://7rfbtov049.execute-api.us-east-1.amazonaws.com/dev")
        }
)

public class OpenApiConfig extends Application {
}
