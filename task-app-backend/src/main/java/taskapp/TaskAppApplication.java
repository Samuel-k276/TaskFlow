package taskapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskAppApplication {
   public static void main(String[] args) {
      try {
         Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
         
         dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
         });
      } catch (Exception e) {
         System.out.println("Warning: Could not load .env file. Using environment variables from system.");
      }
      
      SpringApplication.run(TaskAppApplication.class, args);
   }
}
