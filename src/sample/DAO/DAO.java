package sample.DAO;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Data access layer interface. Provides general functionality to connect and perform basic database CRUD operations.
 * @param <T> Represents the genetic type to be used within the interface.
 * @author Peter Joseph D'Angelo.
 */
public interface DAO<T> {

    public Connection getConnection();

    public ObservableList<T> get() throws SQLException;

    public void closeConnection();

    public T get(int id) throws SQLException;

    public void insert(T t) throws SQLException;

    public void update(T t) throws SQLException;
    public int delete(int id) throws SQLException;


}
