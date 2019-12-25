package com.example.android.moviesapp.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.moviesapp.model.AllData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

// Converter class for List<AllData>
public class Convertor {

    @TypeConverter
    public static List<AllData> fromString(String value) {
        Type listType = new TypeToken<List<AllData>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<AllData> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}