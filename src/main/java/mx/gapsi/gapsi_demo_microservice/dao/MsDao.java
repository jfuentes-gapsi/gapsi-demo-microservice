package mx.gapsi.gapsi_demo_microservice.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.gapsi.commons.model.Base;
import mx.gapsi.commons.utils.DbQueries;
import mx.gapsi.gapsi_demo_microservice.model.Label;

@Repository
public class MsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String FIND_ALL = "SELECT LABEL_ID, KEY, VALUE, LANGUAGE, CREATED, USER_CREATOR, UPDATED, USER_MODIFIER FROM LABELS";

    public Base findAll(Base base) {
        base.setSuccessfully(false);
        List<Label> collection = new ArrayList<>();
        String query = DbQueries.buidlQuery(base, FIND_ALL);
        HashMap<String, String> search = Objects.isNull(base.getSearch()) ? null : base.getSearch().getParameters();
        collection = jdbcTemplate.query(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query);
                        if (!Objects.isNull(search) && !search.isEmpty()) {
                            search.remove("searchType");
                            int i = 1;
                            for (String column : Objects.requireNonNull(search).keySet()) {
                                ps.setObject(i, search.get(column));
                                i++;
                            }
                        }
                        return ps;
                    },new BeanPropertyRowMapper<>(Label.class)
            );

        return base;
    }
    
}
