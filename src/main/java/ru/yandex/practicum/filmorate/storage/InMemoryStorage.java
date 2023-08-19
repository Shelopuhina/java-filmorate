package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryStorage implements Storage {

    private final HashMap<Integer, T> storage = new HashMap<>();//не дает оставить просто Т
    private int count = 1;

    @Override
    public <T> create(T data) {//методы создания и обновления должны по тз возвращать объект, потому дополнила
        if (data == null) throw new NotFoundException("Невозможно сохранить пустой объект.");
        data.validate(data);//методы наследников не работаю
        storage.put(count, data);
        data.setId(count);
        count++;
        return data;
    }

    @Override
    public Entity update(Entity data) {
        if (storage.containsKey(data.getId())) {
            data.validate(data);
            storage.put(data.getId(), data);
            log.debug("Объект с id " + data.getId() + " обновлен.");
        } else {
            throw new NotFoundException("Сначала добавьте объект в систему.");
        }
        return data;
    }

    @Override
    public Entity get(int id) {
        if (storage.get(id) == null) {
            throw new ValidationException("Объект с id " + id + "не найден.");
        }
        Entity data = storage.get(id);
        data.validate(data);
        return data;
    }
    

    @Override
    public List<Entity> getAll() {
        return new ArrayList<>(storage.values());
    }
}










