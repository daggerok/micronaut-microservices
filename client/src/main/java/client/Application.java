package client;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.Micronaut;
import io.reactivex.Single;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class Greeting {
  private String message;
}

@Client("http://127.0.0.1:8082/hello")
interface ServerClient {

  @Get("/{name}")
  Single<Greeting> hello(String name);
}

@Controller("/greet")
class ClientResource {

  private final ServerClient serverClient;

  ClientResource(ServerClient serverClient) {
    this.serverClient = serverClient;
  }

  @Get("/{name}")
  Single<Greeting> greet(String name) {
    return serverClient.hello(name);
  }
}

public class Application {

  public static void main(String[] args) {
    Micronaut.run(Application.class);
  }
}
