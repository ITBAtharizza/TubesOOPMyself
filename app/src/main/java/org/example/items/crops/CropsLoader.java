package org.example.items.crops;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class CropsLoader {

    public static List<Crops> load() {
        String fullPath = "src/main/resources/crops.json";

        try {
            String jsonContent = Files.readString(Paths.get(fullPath));

            Type listType = new TypeToken<List<CropsData>>() {}.getType();
            List<CropsData> cropsDataList = new Gson().fromJson(jsonContent, listType);

            List<Crops> cropsList = new ArrayList<>();
            for (CropsData data : cropsDataList) {
                Crops crop = new Crops(data.name, data.harvestedAmount, data.sellPrice, data.buyPrice);
                cropsList.add(crop);
            }

            return cropsList;
        } catch (JsonSyntaxException | IOException e) {
            throw new RuntimeException("Failed to load or parse JSON file at: " + fullPath, e);
        }
    }
}
