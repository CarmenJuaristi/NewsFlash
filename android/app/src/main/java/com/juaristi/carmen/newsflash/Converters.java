package com.juaristi.carmen.newsflash;
import androidx.room.TypeConverter;

public class Converters {

    // Convierte el objeto Source a un String
    @TypeConverter
    public String fromSource(Source source) {
        if (source == null) {
            return null;
        }
        return source.getName(); // Asumiendo que "Source" tiene un método getName()
    }

    // Convierte un String a un objeto Source
    @TypeConverter
    public Source toSource(String name) {
        if (name == null) {
            return null;
        }
        return new Source(name, name); // Asumiendo que Source tiene un constructor que toma dos String
    }
}
