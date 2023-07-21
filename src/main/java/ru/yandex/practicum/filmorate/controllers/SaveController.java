package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class SaveController implements Handler{
File newFile = new File("C:\\Users\\T-3000\\dev\\filmorate\\src\\main\\java\\ru\\yandex\\practicum\\filmorate\\controllers", "fileForData.txt");
    protected void save(File newFile) {
        try (Writer file = new FileWriter(newFile.getName())){
            file.write("id,name,description,releaseDate,duration\n");
            for (SimpleTask simpleTask : getSimpleTask().values()) {
                file.write(toString(simpleTask));
            }
            for (Epic epic : getEpics().values()) {
                file.write(toString(epic));
            }
            for (SubTask subTask : getSubTasks().values()) {
                file.write(toString(subTask));
            }
            file.write("\n");
            file.write(historyToString(super.getManagerHistory()));
        } catch (IOException exc) {
            throw new ManagerSaveException("Ошибка сохраенения в файл");
        }
    }
    public  FileBackedTasksManager  loadFromFile(File file) {
        FileBackedTasksManager newmanager = new FileBackedTasksManager();
        try  (FileReader reader = new FileReader(file.getName())) {
            BufferedReader br = new BufferedReader(reader);
            List <String> allStrings = new ArrayList<>();
            while (br.ready()) {
                String line = br.readLine();
                allStrings.add(line);
            }
            int maxNextId = 0;
            for (int i = 1; i < (allStrings.size()-1); i++) {
                if(!(allStrings.get(i).isEmpty())) {
                    Task newTask = newmanager.fromString(allStrings.get(i));
                    if (newTask.getId() > maxNextId) {
                        maxNextId = newTask.getId();
                    }
                    String[] split = allStrings.get(i).split(",");
                    int epicId;
                    if (newTask.getType().equals(TaskType.SUBTASK)) {
                        epicId = Integer.parseInt(split[8]);
                        SubTask subTask = new SubTask(newTask.getName(), newTask.getDescription(), newTask.getId(),
                                newTask.getStatus(),newTask.getDuration(),newTask.getStartTime(),epicId);
                        newmanager.getSubTasks().put(subTask.getId(), subTask);
                        List<Integer> subsId = newmanager.getEpicById(epicId).getSubsId();
                        subsId.add(newTask.getId());
                    } else if (newTask.getType().equals(TaskType.EPIC)) {
                        Epic epic = new Epic(newTask.getName(), newTask.getDescription(), newTask.getId());
                        newmanager.getEpics().put(epic.getId(), epic);
                    } else {
                        SimpleTask simpleTask = new SimpleTask(newTask.getName(), newTask.getDescription(), newTask.getId(),
                                newTask.getStatus(),newTask.getDuration(),newTask.getStartTime());
                        newmanager.getSimpleTask().put(simpleTask.getId(), simpleTask);
                    }

                }else{
                    String nextString = allStrings.get(i+1);
                    List<Integer> newHistory = historyFromString(nextString);

                    for (Integer id : newHistory) {
                        if(newmanager.getSimpleTask().containsKey(id)) {
                            newmanager.getTaskById(id);
                        }else if(newmanager.getEpics().containsKey(id)) {
                            newmanager.getEpicById(id);
                        }else{
                            newmanager.getSubTaskById(id);
                        }
                    }

                }
            }
            newmanager.nextId = maxNextId + 1;
        } catch (IOException exc) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        return newmanager;
    }

    @Override
    public Object add(Object obj) {
        if(obj.getClass().equals(Film.class)) {
            
        }else if(obj.getClass().equals(User.class)) {

        }else{
            throw new NotExpectedException("Попытка добавить неизвестный объект.");
        }
        return null;
    }

    @Override
    public Object update(Object obj) {
        if(obj.getClass().equals(Film.class)) {

        }else if(obj.getClass().equals(User.class)) {

        }else{
            throw new NotExpectedException("Попытка обновить неизвестный объект.");
        }
        return null;
    }

    @Override
    public List<Object> getList() {

        return null;
    }
}
