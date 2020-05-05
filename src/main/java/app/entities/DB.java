package app.entities;

import java.sql.*;
import org.sqlite.*;

public class DB {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;


    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void connection() throws ClassNotFoundException, SQLException {
        if (DB.conn != null) {
            return;
        }
        Class.forName("org.sqlite.JDBC");
        DB.conn = DriverManager.getConnection("jdbc:sqlite:" + "D:/lab_web/colleen.music.s3db");
        System.out.println("База Подключена!");
        DB.statmt = DB.conn.createStatement();
    }

    // --------Заполнение таблицы--------
    public static boolean execute(String sql) throws SQLException {//sql строка
        return DB.statmt.execute(sql);
    }

    // -------- Вывод таблицы--------
    public static ResultSet executeQuery(String sql) throws SQLException {//массив строк
        return DB.statmt.executeQuery(sql);
    }

    // --------Закрытие--------
    public static void close() throws SQLException {
        DB.conn.close();
        DB.statmt.close();
        DB.resSet.close();

        System.out.println("Соединения закрыты");
    }

}
