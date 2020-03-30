package gw;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.Micronaut;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Greeting {
  private String message;
}

@Client("http://127.0.0.1:8082/hello")
interface ServerClient {

  @Get("/{name}")
  Single<Greeting> hello(String name);
}

@Client("http://127.0.0.1:8081")
interface ClientClient {

  @Get("/greet/{name}")
  Single<Greeting> greet(String name);
}

@Controller
class GatewayResources {

  private final ClientClient clientClient;
  private final ServerClient serverClient;

  GatewayResources(ClientClient clientClient,
                   ServerClient serverClient) {

    this.serverClient = serverClient;
    this.clientClient = clientClient;
  }

  @Get("/server/{greeting}")
  Single<Greeting> serverCall(String greeting) {
    return serverClient.hello("from server client");
  }

  @Get("/client/{greeting}")
  Single<Greeting> clientCall(String greeting) {
    return clientClient.greet("from client client");
  }

  @Get
  Flowable<Greeting> clientCall() {
    return serverClient.hello("server")
                       .mergeWith(clientClient.greet("client"));
  }
}

public class Application {

  public static void main(String[] args) {
    Micronaut.run(Application.class);
  }
}
