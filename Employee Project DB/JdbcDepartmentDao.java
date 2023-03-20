import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;

public class JdbcDepartmentDao implements DepartmentDao {
	
	private final JdbcTemplate jdbcTemplate;

	public JdbcDepartmentDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Department getDepartment(int id) {
		Department department = null;
		String sql = "SELECT department_id, name FROM department WHERE department_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		if (results.next()) {
			department = mapRowToDepartment(results);
		}
		return department;
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> department = new ArrayList<>();
		String sql = "SELECT department_id, name FROM department;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
		while(result.next()) {
			department.add(mapRowToDepartment(result));
		}
		return department;
	}

	@Override
	public void updateDepartment(Department updatedDepartment) {
		String sql = "UPDATE department SET name = ? WHERE department_id = ?;";
		jdbcTemplate.update(sql, updatedDepartment.getName(), updatedDepartment.getId());
	}

	private Department mapRowToDepartment(SqlRowSet rs) {
		Department department = new Department();
		department.setId(rs.getInt("Department_id"));
		department.setName(rs.getString("name"));
		return department;
	}

}
