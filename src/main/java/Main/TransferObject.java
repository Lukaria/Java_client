package Main;

import com.fasterxml.jackson.core.JsonProcessingException;
import enums.Commands;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferObject<T> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private Commands command;
    private T object;

    public TransferObject(){};

    public TransferObject(T object, Commands command) {
        this.command = command;
        this.object = object;
    }

    public boolean sendObjectTo(T object, Commands command, ObjectOutputStream stream) {
        try {
            TransferObject sendObject = new TransferObject(object, command);
            String message = objectMapper.writeValueAsString(sendObject);
            stream.writeObject(message);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public TransferObject getObjectFrom(ObjectInputStream stream) {
        TransferObject<T> object = null;
        try {
            String message = (String) stream.readObject();

            object = objectMapper.readValue(message, TransferObject.class);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public boolean sendList(List<T> objects, Commands command, ObjectOutputStream stream) throws JsonProcessingException {
        List<TransferObject> toList = new ArrayList<>();
        try{
            for(T list: objects){
                toList.add(new TransferObject(list, command));
            }

            String message = objectMapper.writeValueAsString(toList);
            stream.writeObject(message);
            return true;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TransferObject> getList(ObjectInputStream stream){
        try{
            String message = (String) stream.readObject();
            TransferObject[] array = objectMapper.readValue(message, TransferObject[].class);
            List<TransferObject> toList = Arrays.asList(array);
            return toList;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@modelClass")
    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public Commands getCommand() {
        return command;
    }

    public void setCommand(Commands command) {
        this.command = command;
    }

    public void nullAll(){
        this.command = null;
        this.object = null;
    }
}
