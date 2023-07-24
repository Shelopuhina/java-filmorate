package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.model.Entity;

import java.util.*;

@Getter
@Slf4j
public class DataController<T extends Entity> {
    private final HashMap<Integer, T> storage = new HashMap<>();
    private int count = 1;


    protected void add(T obj) {
        if (obj == null) throw new NotExpectedException("Невозможно сохранить пустой объект.");
        obj.isValidate(obj);
        if (storage.containsKey(obj.getId()))
            throw new NotExpectedException("Объект с id " +
                    obj.getId() + " уже зарегистрирован.");
        obj.setId(count);
        count++;
        storage.put(obj.getId(), obj);
        log.debug("Объект с id " + obj.getId() + " добавлен в систему.");
    }

    protected void update(T obj) {
        if (storage.containsKey(obj.getId())) {
            obj.isValidate(obj);
            storage.put(obj.getId(), obj);
            log.debug("Объект с id " + obj.getId() + " обновлен.");
        } else {
            throw new NotExpectedException("Сначала добавьте объект в систему.");
        }
    }

    protected List<T> getList() {
        return new ArrayList<T>(storage.values());
    }
}