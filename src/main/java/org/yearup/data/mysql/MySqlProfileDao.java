package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getProfile(int userId) {

        String sql = """
                    SELECT
                    first_name
                    ,last_name
                    ,phone
                    ,email
                    ,address
                    ,city
                    ,state
                    ,zip
                    FROM\s
                    profiles
                    WHERE\s
                    user_id = ?
                   \s""";


        try(Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                Profile profile = new Profile();

                profile.setFirstName(resultSet.getString(1));
                profile.setLastName(resultSet.getString(2));
                profile.setPhone(resultSet.getString(3));
                profile.setEmail(resultSet.getString(4));
                profile.setAddress(resultSet.getString(5));
                profile.setCity(resultSet.getString(6));
                profile.setState(resultSet.getString(7));
                profile.setZip(resultSet.getString(8));



                return profile;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @Override
    public Profile editProfile(Profile profile) {
        String sql = """
                UPDATE
                profiles
                SET
                first_name = ?
                ,last_name = ?
                ,phone = ?
                ,email = ?
                ,address = ?
                ,city = ?
                ,state = ?
                ,zip = ?
                WHERE\s
                user_id = ?
                """;

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);)
        {


            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getPhone());
            ps.setString(4, profile.getEmail());
            ps.setString(5, profile.getAddress());
            ps.setString(6, profile.getCity());
            ps.setString(7, profile.getState());
            ps.setString(8, profile.getZip());
            ps.setInt(9, profile.getUserId());

            int rows = ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

}
