package com.techelevator.projects.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.techelevator.projects.model.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;

public class JdbcProjectDao implements ProjectDao {

	private final JdbcTemplate jdbcTemplate;

	public JdbcProjectDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Project getProject(int projectId) {
		Project project = null;
		String sql = "SELECT project_id, name, from_date, to_date FROM project WHERE project_id = ?;";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, projectId);
		if (results.next()) {
			project = mapRowToProject(results);
		}

		return project;
	}

	@Override
	public List<Project> getAllProjects() {
		List<Project> projects = new ArrayList<>();
		String sql = "SELECT project_id, name, from_date, to_date FROM project;";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Project project = mapRowToProject(results);
			projects.add(project);
		}

		return projects;
	}

	@Override
	public Project createProject(Project newProject) {
		String sql = "INSERT INTO project (name, from_date, to_date) VALUES (?, ?, ?) RETURNING project_id;";
		Integer projectId = jdbcTemplate.queryForObject(sql, Integer.class, newProject.getName(), newProject.getFromDate(), newProject.getToDate());
		Project project = getProject(projectId);
		return project;
	}

	@Override
	public void deleteProject(int projectId) {
		String deleteProjectEmployeeSql = "DELETE FROM project_employee WHERE project_id=?;";
		jdbcTemplate.update(deleteProjectEmployeeSql, projectId);

		String deleteProjectSql = "DELETE FROM project WHERE project_id=?;";
		jdbcTemplate.update(deleteProjectSql, projectId);
	}

	private Project mapRowToProject(SqlRowSet rs) {
		Project project = new Project();
		project.setId(rs.getInt("project_id"));
		project.setName(rs.getString("name"));
		Date fromDate = rs.getDate(("from_date"));
		if (fromDate != null) {
			project.setFromDate(rs.getDate("from_date").toLocalDate());
		}
		Date toDate = rs.getDate("to_date");
		if (toDate != null) {
			project.setToDate(rs.getDate("to_date").toLocalDate());
		}

		return project;
	}

}
