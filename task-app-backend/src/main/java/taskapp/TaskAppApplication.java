package taskapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskAppApplication {
   public static void main(String[] args) {
      // Carrega as variáveis do arquivo .env
      Dotenv dotenv = Dotenv.configure().load();
      
      // Transfere as variáveis do .env para as propriedades do sistema
      dotenv.entries().forEach(entry -> {
         System.setProperty(entry.getKey(), entry.getValue());
      });
      
      SpringApplication.run(TaskAppApplication.class, args);
   }
}
