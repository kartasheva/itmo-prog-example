package ru.ifmo.prog.lab5;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class Collection<T> {
    private String pathToFile; //имя файла
    private Class<T> itemType;
    private ObjectMapper mapper = new ObjectMapper();
    private Hashtable<String, T> store = new Hashtable<>();

    public Collection(String pathToFile, Class<T> itemType) throws IOException {
        this.pathToFile = pathToFile;
        this.itemType = itemType;
        load();
    }

    /**
     * Перечитать коллекцию из файла
     * @throws IOException
     */
    public void load() throws IOException {
        File file = new File(pathToFile);
        StringBuilder content = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                content.append(line);
            }
        }

        MapType mapType = mapper.getTypeFactory().constructMapType(Hashtable.class, String.class, itemType);
        store = mapper.readValue(content.toString(), mapType);
    }

    /**
     * Сохранить коллекцию в файл
     * @throws IOException
     */
    public void save() throws IOException {
       String out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(store);
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile))) {
           writer.write(out);
           writer.flush();
       }
    }

    /**
     * Очистить коллекцию
     */
    public void clear() {
        store.clear();
    }


    /**
     * Удалить из коллекции все элементы, эквивалентные заданному
     * @param elemnt значение элементов для удаления
     * @return boolean результат удаления
     */
    public Boolean removeAll(T elemnt) {
        Boolean result = false;
        for (int i = 0; i < store.values().size(); i++) {
            if (store.values().toArray()[i].equals(elemnt)) {
                store.remove(store.keySet().toArray()[i]);
                result = true;
            }
        }

        return result;
    }

    /**
     * Удалить элемент из коллекции по его ключу
     * @param key ключ удаляемого элемента
     * @return значение удаляемого элемента
     */
    public T remove(String key) {
        return store.remove(key);
    }

    /**
     * Получить значения в коллекции
     * @return коллекция значений
     */
    public java.util.Collection<T> values() {
        return store.values();
    }

    /**
     * Вывести содержимое коллекции
     */
    public void show() {
        store.forEach((key, item) -> {
            try {
                System.out.printf("%s, %s%n", key, mapper.writeValueAsString(item));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
