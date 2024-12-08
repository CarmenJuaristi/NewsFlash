package com.juaristi.carmen.newsflash;

public abstract class Resource<T> {

    private T data;
    private String message;

    public Resource(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class Success<T> extends Resource<T> {
        public Success(T data) {
            super(data, null);  // No hay mensaje en éxito
        }
    }

    public static class Error<T> extends Resource<T> {
        public Error(String message) {
            super(data, message);
        }
    }

    public static class Loading<T> extends Resource<T> {
        public Loading() {
            super(null, null);  // No hay datos ni mensaje en "Cargando"
        }
    }
}
