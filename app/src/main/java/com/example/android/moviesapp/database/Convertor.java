package com.example.android.moviesapp.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.moviesapp.model.MovieData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

// Converter class for List<MovieData>
public class Convertor {

    @TypeConverter
    public static List<MovieData> fromString(String value) {
        Type listType = new TypeToken<List<MovieData>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<MovieData> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}