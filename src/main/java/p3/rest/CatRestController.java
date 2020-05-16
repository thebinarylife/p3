package p3.rest;
import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import p3.jdbctemplate.dao.CatDAO;
import p3.jdbctemplate.model.Cat;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/rest/v1/cats")
public class CatRestController {	
	@Autowired
	@Qualifier("catDAOImpl")
	private CatDAO catDAO;
	@RequestMapping(value="/echoMessage", method=RequestMethod.GET)
	@ApiOperation(value = "To test this REST end point is alive via echo",
			  notes = "An optional message value can be passed into this echo service, via msg query param, to use in echoed String",
			  response = String.class)
	
	
	public String echoMessage(@ApiParam(value = "Hello", required = false) @RequestParam(value="msg", defaultValue="Mike") String msg) {
			return "echoMessage echoed: " + msg;
	}
	
	@GetMapping({"/","/all"})
	public  List<Cat> findAll() {
		List<Cat> cats = catDAO.findAll();
		
		return cats;
	}
	
	@GetMapping({"/byType/{type}"})
	public  Optional<List<Cat>> findByType(@PathVariable String type) {
		Optional<List<Cat>> cats = catDAO.findByType(type);
		
		return cats;
	}
	
	@GetMapping({"/{id}"})
	public  Optional<Cat> findById(@PathVariable Long id) {
		Optional<Cat> cats = catDAO.findById(id);
		
		return cats;
	}

	
}
