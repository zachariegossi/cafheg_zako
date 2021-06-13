package ch.hearc.cafheg.infrastructure.application;

import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ch.hearc.cafheg")
public class Application extends SpringBootServletInitializer {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    start();
    SpringApplication.run(Application.class, args);
  }

  private static void start() {
    Database database = new Database();
    Migrations migrations = new Migrations(database);

    logger.debug("Hello from Logback");
    logger.error("Erreur from LogBack", new NullPointerException());
    database.start();
    migrations.start();

    try {
      System.out.println(getData());
    } catch (IllegalArgumentException e) {
      logger.error("ERROR {}", e);
      logger.info("INFO {}", e);
    }

  }

  static int getData() throws IllegalArgumentException {
    throw new IllegalArgumentException("Sorry IllegalArgumentException!");
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    super.onStartup(servletContext);
    start();
  }
}
