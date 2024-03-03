package org.jdbc_example;

import java.sql.*;

public class MainClass {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;

    public static void main(String[] args) {
        try {
            connect();

            insertEx();
            // selectEx();
            // updateEx();
            // deleteEx();
            // dropTableEx();

            // transactionsAndPreparedStatementEx();
            // bathEx();

            // rollBack();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }


    private static void insertEx() throws SQLException {
        statement.executeUpdate("INSERT INTO students (name, grade) VALUES ('Artem', 11);");
    }

    private static void selectEx() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM students;");
        try {
            while (rs.next()) {
                System.out.println(rs.getInt(1) +
                        " " + rs.getString("name") +
                        " " + rs.getInt("grade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateEx() throws SQLException {
        statement.executeUpdate("UPDATE students SET grade = 11 WHERE id = 1");
    }

    private static void deleteEx() throws SQLException {
        //Удалить только одну запись
        //statement.executeUpdate("DELETE FROM students WHERE id = 3");

        //Удалить все записи
        statement.executeUpdate("DELETE FROM students");
    }

    private static void dropTableEx() throws SQLException {
        statement.executeUpdate("DROP TABLE students");
    }

    private static void transactionsAndPreparedStatementEx() throws SQLException {

        connection.setAutoCommit(false); // Выключили автокомит, чтобы транзакции не записывались в базу по отдельности.

        for (int i = 1; i <= 10; i++) {
            preparedStatement.setString(1, "Bob - " + i);
            preparedStatement.setInt(2, i);
            //preparedStatement.setObject(); МОЖНО ДОБАВЛЯТЬ ОБЪЕКТ ЛЮБОГО ТИПА
            preparedStatement.execute();
        }

        connection.commit(); // Делаем общий коммит, чтобы закинуть все транзакции скопом.

    }

    private static void bathEx() throws SQLException {
        for (int i = 1; i <= 10; i++) {
            preparedStatement.setString(1, "Bob - " + i);
            preparedStatement.setInt(2, i);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch(); // Используется для передачи всех команд для обработки одноим махом.
    }

    private static void rollBack() throws SQLException {
        statement.executeUpdate("INSERT INTO students (name, grade) VALUES ('Ivan1', 7)");
        Savepoint sp1 = connection.setSavepoint(); // Сохраянемся, чтобы была возможность откатиться к этой точке с помощью rollback.
        statement.executeUpdate("INSERT INTO students (name, grade) VALUES ('Ivan2', 7)");
        connection.rollback(sp1); // Откатываемся к точке sp1.
        statement.executeUpdate("INSERT INTO students (name, grade) VALUES ('Ivan3', 7)");
        connection.commit(); // Когда мы сделали setSavepoint(), то автокоммит выключается. соответственно, чтобы добавить Ivan3 мы должны сделать commit в ручную.
    }


    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Курсы\\Java\\SQLite\\main.db");
            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement("INSERT INTO students (name, grade) VALUES (?, ?);");
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Enable to connect !");
        }
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
