package mx.gapsi.gapsi_demo_microservice.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.gapsi.commons.model.Base;
import mx.gapsi.commons.utils.DbQueries;
import mx.gapsi.commons.utils.InitObject;
import mx.gapsi.gapsi_demo_microservice.model.Label;

@Repository
public class MsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String FIND_ALL = "SELECT LABEL_ID, KEY, VALUE, LANGUAGE, CREATED, USER_CREATOR, UPDATED, USER_MODIFIER FROM LABELS";
    private final static String INSERT = "INSERT INTO LABELS(KEY, VALUE, LANGUAGE, CREATED, USER_CREATOR) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?) RETURNING LABEL_ID";
    private final static String UPDATE = "UPDATE LABELS SET VALUE = ?, LANGUAGE = ?, USER_MODIFIER = ?, UPDATED = CURRENT_TIMESTAMP, KEY= ? WHERE LABEL_ID= ?";
    private final static String DELETE = "DELETE FROM LABELS";

    public Base findAll(Base base) {
        base.setSuccessfully(false);
        List<Label> collection;
        String baseQuery = DbQueries.buildQuery(base, FIND_ALL);
        String query = baseQuery + 
                        DbQueries.buildOrderBy(base) + 
                        DbQueries.buildPagination(base);
        HashMap<String, String> search = Objects.isNull(base.getSearch()) ? null : base.getSearch().getParameters();
        collection = jdbcTemplate.query(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query);
                        return DbQueries.buildStatement(search, ps);
                    },new BeanPropertyRowMapper<>(Label.class)
                );
        List<Object> objectList = new ArrayList<>(collection);
        int totalRows = DbQueries.getTotalRows(baseQuery, search, jdbcTemplate);
        base.setCustomDto(InitObject.toCustomDto(base, totalRows));
        base.getCustomDto().setData(objectList);
        base.setSuccessfully(true);

        return base;
    }

    public Base insert(Base base) {
        base.setSuccessfully(false);
        List<Object> collection = new ArrayList<>();
        Class<Label> labelClass = Label.class;
        Label label = labelClass.cast(base.getData());
        label.setLabelId(jdbcTemplate.queryForObject(INSERT, Long.class,
							new Object[] { label.getKey(), 
									label.getValue(),
									label.getLanguage(),
									label.getUserCreator()}));
        collection.add(label);
        base.getCustomDto().setData(collection);
        base.setSuccessfully(true);

        return base;
    }

    public Base update(Base base) {
        int rowAffected = 0;
        base.setSuccessfully(false);
        List<Object> collection = new ArrayList<>();
        Class<Label> labelClass = Label.class;
        Label label = labelClass.cast(base.getData());
        rowAffected = jdbcTemplate.update(UPDATE, new Object[] {
					                label.getValue(),
                                    label.getLanguage(),
                                    label.getUserModifier(),
                                    label.getKey(),
                                    label.getLabelId()});
        if (rowAffected > 0) {
            base.setSuccessfully(true);
        }
        collection.add(label);
        base.getCustomDto().setData(collection);

        return base;
    }

    public Base delete(Base base) {
        int rowAffected = 0;
		String queryIn = DbQueries.buildQueryIn(DELETE, base.getItems(), "LABEL_ID");
        base.setSuccessfully(false);
        rowAffected = jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(queryIn);
				return ps;
			});
        if (rowAffected > 0) {
            base.setSuccessfully(true);
        }

        return base;
    }
}
