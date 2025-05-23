package org.example.items.food;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class FoodLoader {
    public static List<Food> load() {
        String fullPath = "src/main/resources/food.json";

        try {
            String jsonContent = Files.readString(Paths.get(fullPath));

            Type listType = new TypeToken<List<FoodData>>() {}.getType();
            List<FoodData> foodDataList = new Gson().fromJson(jsonContent, listType);

            List<Food> foodList = new ArrayList<>();
            for (FoodData data : foodDataList) {
                Food food = new Food(data.name, data.energyGained, data.sellPrice, data.buyPrice);
                foodList.add(food);
            }

            return foodList;
        } catch (JsonSyntaxException | IOException e) {
            throw new RuntimeException("Failed to load or parse JSON file at: " + fullPath, e);
        }
    }
}
