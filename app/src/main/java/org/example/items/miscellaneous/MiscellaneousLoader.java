package org.example.items.miscellaneous;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class MiscellaneousLoader {

    public static List<Miscellaneous> load() {
        String fullPath = "src/main/resources/miscellaneous.json"; 

        try {
            String jsonContent = Files.readString(Paths.get(fullPath));

            Type listType = new TypeToken<List<MiscellaneousData>>() {}.getType();
            List<MiscellaneousData> miscellaneousDataList = new Gson().fromJson(jsonContent, listType);

            List<Miscellaneous> miscellaneousList = new ArrayList<>();
            for (MiscellaneousData data : miscellaneousDataList) {
                Miscellaneous miscellaneous = new Miscellaneous(data.name, data.sellPrice, data.buyPrice);
                miscellaneousList.add(miscellaneous);
            }

            return miscellaneousList;
        } catch (JsonSyntaxException | IOException e) {
            throw new RuntimeException("Failed to load or parse JSON file at: " + fullPath, e);
        }
    }
}
