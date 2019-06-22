package pl.sda.jdbcjpa;

import java.sql.*;


public class JdbcMain {
    public static void main(String[] args) {
        statement();
        prepareStatement();
        callableStatement();

    }

    private static void callableStatement() {
        try(Connection connection = getConnection()) {
            String query = " {call sdajdbc.getName(?,?)}";
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, 7654);
            callableStatement.registerOutParameter(2,Types.VARCHAR);

            callableStatement.execute();
            String string = callableStatement.getString(2);
            System.out.println(string);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void prepareStatement() {
        try (Connection connection = getConnection()) {
            int minSalary = 1000;
            String query = "select ename, job, sal " +
                    "from sdajdbc.employee " +
                    "where sal > ?"; // znak zapytania bezpieczny bo jak by było sal >" minSalary to ktoś może coś dopisać np. drop DB
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, minSalary);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ename = resultSet.getString("ename");
                String job = resultSet.getString("job");
                int sal = resultSet.getInt("sal");
                System.out.println(ename + " " + job + " " + sal);
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void statement() {
        try (Connection connection = getConnection()) {
            String query = "select ename, job, sal " +
                    "from sdajdbc.employee " +
                    "where sal > 100";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String ename = resultSet.getString("ename");
                String job = resultSet.getString("job");
                int sal = resultSet.getInt("sal");
                System.out.println(ename + " " + job + " " + sal);
            }

            // można tutaj podać dowolone zapytanie,
            // chociaz rozwiązanie wydaje się być przestarzałe (jdbc- podstawa działania JPA i Hybernate)


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "nigerhaza8");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
