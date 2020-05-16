package p3.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import p3.jpa.repo.DogRepository;
import p3.jpa.model.Dog;

@RestController
@RequestMapping("/rest/v1/dogs")
public class DogRestController {
	
	
	@Autowired
	private DogRepository dogRepository;
	@RequestMapping(value="/echoMessage", method=RequestMethod.GET)
	
	@ApiOperation(value = "To test this REST end point is alive via echo",
			  notes = "An optional message value can be passed into this echo service, via msg query param, to use in echoed String",
			  response = String.class)
	
	public String echoMessage(@ApiParam(value = "Hello", required = false) @RequestParam(value="msg", defaultValue="Hello") String msg)  {	
		return "echoMessage echoed: " + msg;
	}
	
	@GetMapping("")
	public Page<Dog> findAll(@RequestParam(defaultValue="0") int page, @RequestParam(value="rowsPerPage", defaultValue="2") int size) {
		Page<Dog> dogPage = dogRepository.findAll(PageRequest.of(page, size));
		
		return dogPage;
	}
	@GetMapping("/all")
	public  List<Dog> findAll() {
		List<Dog> dogs = dogRepository.findAll();
		
		return dogs;
	}
	@GetMapping({"/{id}"})
	public  Optional<Dog> findById(@PathVariable Long id) {
		Optional<Dog> dog = dogRepository.findById(id);
		
		return dog;
	}
	
	@GetMapping("/byType/{type}")
	public  Optional<List<Dog>> findByType(@PathVariable String type) {
		Optional<List<Dog>> dogs = dogRepository.findByType(type);
		
		return dogs;
	}
}

