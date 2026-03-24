package co.edu.uptc.api_rest.storage;

import co.edu.uptc.api_rest.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductFileStorage {

    @Value("${app.storage.dir}")
    private String directory;

    @Value("${app.storage.file}")
    private String filename;

    public List<Product> readProducts() {
        File file = getFile();

        if (!file.exists()) {
            return new ArrayList<>();
        }

        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 3);
                if (parts.length != 3) continue;

                Long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());

                products.add(new Product(id, name, price));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el archivo", e);
        }

        return products;
    }

    public void saveProducts(List<Product> products) {
        File file = getFile();
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (Product p : products) {
                writer.write(p.getId() + "," + p.getName() + "," + p.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo el archivo", e);
        }
    }

    private File getFile() {
        File dir =  new File(directory);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new File(dir, filename);
    }
}
