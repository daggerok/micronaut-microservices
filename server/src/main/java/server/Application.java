package server;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.Micronaut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
class Greeting {
  private String message;
}

@Controller("/hello")
class ServerResource {

  @Get("/{name}")
  Greeting hello(String name) {
    System.out.println("handling hello for " + name);
    String message = String.format("Hello, %s!", name);
    return Greeting.of(message);
  }
}

public class Application {

  public static void main(String[] args) {
    Micronaut.run(Application.class);
  }
}
