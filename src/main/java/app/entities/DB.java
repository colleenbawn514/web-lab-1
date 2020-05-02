package app.entities;

import java.sql.*;
import org.sqlite.*;

public class DB {
    public Connection conn;
    public Statement statmt;
    public ResultSet resSet;

    public DB(String nameDB) throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + nameDB);

        System.out.println("База Подключена!");
        statmt = conn.createStatement();
    }

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static DB connection(String nameDB) throws ClassNotFoundException, SQLException {
        return new DB("D:/lab_web/colleen.music.s3db");
    }

    // --------Заполнение таблицы--------
    public boolean execute(String sql) throws SQLException {//sql строка
        return statmt.execute(sql);
    }

    // -------- Вывод таблицы--------
    public ResultSet executeQuery(String sql) throws SQLException {//массив строк
        return statmt.executeQuery(sql);
    }

    // --------Закрытие--------
    public void close() throws SQLException {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }

}
