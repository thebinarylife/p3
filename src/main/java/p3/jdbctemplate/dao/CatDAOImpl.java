
package p3.jdbctemplate.dao;

import java.util.List;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import p3.jdbctemplate.model.Cat;
import p3.jdbctemplate.model.CatRowMapper;

@Repository
@Transactional
public class CatDAOImpl implements CatDAO {

	private static final Logger logger = LoggerFactory.getLogger(CatDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Cat> findAll() {
		String sql = "SELECT e.id, e.name, e.type FROM cats e";
		RowMapper<Cat> rowMapper = new BeanPropertyRowMapper<Cat>(Cat.class);

		return jdbcTemplate.query(sql, rowMapper);
	}

	public Optional<Cat> findById(Long id) {
		Optional<Cat> oCat = Optional.ofNullable(null);
		String sql = "SELECT e.id, e.name, e.type FROM cats e WHERE e.id = ?";

		if (id == null)
			logger.warn("findById unExpected null input, will do nothing, will return nulled Optional");
		else {
			RowMapper<Cat> rowMapper = new BeanPropertyRowMapper<Cat>(Cat.class);
			Cat cats = jdbcTemplate.queryForObject(sql, rowMapper, id);

			oCat = Optional.ofNullable(cats);
		}

		return oCat;
	}

	public Optional<Cat> findCat(Cat cats) {
		Optional<Cat> oCat = Optional.ofNullable(null);

		if (cats == null) {
			logger.warn("findOne unExpected null input,Returning null");
			return null;
		}

		Long id = cats.getId();

		if (id != null)
			oCat = findById(id);
		else {
			String name = cats.getName();
			String type = cats.getType();

			List<Cat> cats1 = findByNameAndType(name, type);

			if (cats1 == null)
				logger.info("findOne could not find specified cat! name:{}, type:{}", name, type);
			else if (cats1.size() >= 1) {
				logger.info("findOne will return 1st cat of {} cats for name:{}, type:{}", cats1.size(), name, type);
				oCat = Optional.of(cats1.get(0));
			}
		}

		return oCat;
	}

	public Cat save(Cat cats) {
		String sqlInsertWithId = "INSERT INTO cats (id, name, type) values (?, ?, ?)";
		String sqlInsertWithoutId = "INSERT INTO cats (name, type) values (?, ?)";
		String sql4id = "SELECT e.id FROM cats e WHERE e.name = ? AND e.type = ?";

		Long id = null;
		if (cats == null)
			return null;

		id = cats.getId();
		if (id != null)
			jdbcTemplate.update(sqlInsertWithId, id, cats.getName(), cats.getType());
		else {
			jdbcTemplate.update(sqlInsertWithoutId, cats.getName(), cats.getType());

			id = jdbcTemplate.queryForObject(sql4id, Long.class, cats.getName(), cats.getType());
			cats.setId(id);
		}

		return cats;
	}

	public void deleteAll() {
		String sql = "DELETE FROM cats";
		jdbcTemplate.update(sql);
	}

	public void deleteById(Long id) {
		if (id == null)
			logger.warn("deleteById unExpected null input, will do nothing");
		else {
			String sql = "DELETE FROM cats e WHERE e.id = ?";
			jdbcTemplate.update(sql, id);
		}
	}

	public boolean existsById(Long id) {
		int count = 0;
		String sql = "SELECT count(*) FROM cats e WHERE e.id = ?";
		if (id != null)
			count = jdbcTemplate.queryForObject(sql, Integer.class, id);

		return count == 0 ? false : true;
	}

	public void delete(Cat cats) {
		if (cats == null) {
			logger.warn("delete unExpected null input, will do nothing");
			return;
		}

		Long id = cats.getId();

		if (id == null) {
			Optional<Cat> oCat = findCat(cats);
			id = oCat.isPresent() ? oCat.get().getId() : null;
			if (id != null)
				deleteById(id);
		} else
			deleteById(id);

	}

	public boolean exists(Cat cats) {
		boolean exists = false;

		if (cats == null)
			return false;

		Long id = cats.getId();
		if (id != null)
			exists = existsById(id);
		else
			exists = existsByNameAndType(cats.getName(), cats.getType());

		return exists;
	}

	public long count() {
		String sql = "SELECT count(*) FROM cats";

		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public Cat add(Cat cats, boolean updateIfExists) {
		if(cats == null)
			return null;
		if (exists(cats)) {
			if (updateIfExists) {
				delete(cats);
				save(cats);
			} else
				logger.info("add will do nothing since employee {} exists in Db", cats.toString());
		} else
			save(cats);

		return cats;
	}

	public Cat update(Cat cats) {
		if(cats == null) {
			logger.warn("update unExpected null input, will do nothing");
			return null;
		}
			if (exists(cats))
				delete(cats);
			save(cats);
		return cats;
	}

	public List<Cat> findByNameAndType(String name, String type) {
		String sql = "SELECT e.id, e.ame, e.type FROM cats e WHERE e.name = ? AND e.type = ?";
		RowMapper<Cat> rowMapper = new CatRowMapper();

		return jdbcTemplate.query(sql, rowMapper, name, type);
	}

	public List<Cat> findByNameOrType(String name, String type) {
		String sql = "SELECT e.id, e.name, e.type FROM cats e WHERE e.name = ? OR e.type = ?";
		RowMapper<Cat> rowMapper = new CatRowMapper();

		return jdbcTemplate.query(sql, rowMapper, name, type);
	}

	public boolean existsByNameAndType(String name, String type) {
		int count = 0;
		String sql = "SELECT count(*) FROM cats e WHERE e.name = ? AND e.typee = ?";
		if (name != null || type != null) {
			count = jdbcTemplate.queryForObject(sql, Integer.class, name, type);
		}
		return count == 0 ? false : true;
	}

	@Override
	public Optional<Cat> findByld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<List<Cat>> findByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
