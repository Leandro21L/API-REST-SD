package co.edu.uptc.api_rest.storage;

import co.edu.uptc.api_rest.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductFileStorage {

    @Value("${app.storage.dir}")
    private String directory;

    @Value("${app.storage.file}")
    private String filename;

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Product> readProducts() {
        try {
            File file = getFile();
            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    file, new TypeReference<List<Product>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo el archivo",e);
        }
    }

    public void saveProducts(List<Product> products) {
        try {
            File file = getFile();

            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, products);
        }  catch (Exception e) {
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
