package org.example.items.fish;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.example.time.GameTime;
import org.example.time.TimeOfDayRange;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class FishLoader {

    public static List<Fish> load() {
        String fullPath = "src/main/resources/fish.json";

        try {
            String jsonContent = Files.readString(Paths.get(fullPath));

            Type listType = new TypeToken<List<FishData>>() {}.getType();
            List<FishData> fishDataList = new Gson().fromJson(jsonContent, listType);

            List<Fish> fishList = new ArrayList<>();
            for (FishData data : fishDataList) {
                FishRarity rarity = FishRarity.valueOf(data.rarity.toUpperCase());
                TimeOfDayRange[] timeRanges = parseTimeRanges(data.timeRanges);
                Fish fish = new Fish(data.name, rarity, data.seasons, data.weathers, data.locations, timeRanges);
                fishList.add(fish);
            }

            return fishList;
        } catch (JsonSyntaxException | IOException e) {
            throw new RuntimeException("Failed to load or parse JSON file at: " + fullPath, e);
        }
    }

    private static TimeOfDayRange[] parseTimeRanges(String[][] timePairs) {
        if (timePairs.length == 1 && timePairs[0].length == 1 && timePairs[0][0].equalsIgnoreCase("any")) {
            return new TimeOfDayRange[] { 
                new TimeOfDayRange(GameTime.MIN, GameTime.MAX) 
            };
        }

        TimeOfDayRange[] ranges = new TimeOfDayRange[timePairs.length];
        for (int i = 0; i < timePairs.length; i++) {
            GameTime start = GameTime.parse(timePairs[i][0]);
            GameTime end = GameTime.parse(timePairs[i][1]);
            ranges[i] = new TimeOfDayRange(start, end);
        }
        
        return ranges;
    }
}
