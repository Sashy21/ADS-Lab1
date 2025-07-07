package server.interfaces;

import java.io.Serializable;

//Allow execution of tasks that bring results- type T
public interface Task<T> extends Serializable {
    T execute() throws Exception;
}