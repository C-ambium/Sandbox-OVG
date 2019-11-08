package com.ovg.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

//import com.ovg.app.exceptions.NoteYetImplementedException;
//import java.sql.SQLException;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ovg.app.entities.Event;
import com.ovg.app.exceptions.BadRequestException;
import com.ovg.app.exceptions.NotFoundException;
import com.ovg.app.repositories.EventRepository;


@RestController
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventRepository repository;

    @GetMapping("list")
    public List<Event> getAll() {
        return this.repository.findAll();
    }

    @PostMapping
    public Event create(@Valid @RequestBody Event event) throws BadRequestException {
        if (this.repository.existsByLabel(event)) {
            throw new BadRequestException("uniq_name");
        }

        return this.repository.save(event);
    }

    @PutMapping("{id}")
    public Event update(@PathVariable Long id, @Valid @RequestBody Event event)
            throws BadRequestException, NotFoundException {
        if (this.repository.existsByLabelIgnoreCaseAndIdNot(event.getLabel(), id)) {
            throw new BadRequestException("uniq_name");
        }

        final Event entity = this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        // TODO: Use mapper.
//        ObjectMapper mapper = new ObjectMapper();
        // ---------
        entity.setLabel(event.getLabel());
        entity.setAuthor(event.getAuthor());
        entity.setDescription(event.getDescription());

        return this.repository.save(entity);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.repository.deleteById(id);
    }

    @GetMapping("{id}")
    public Event getOne(@PathVariable Long id) throws NotFoundException {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }
}

/*
@RestController
@RequestMapping("event")
public class EventController extends CrudController<Event, EventRepository>{

    @Autowired
    private EventRepository repository;

    @GetMapping("/list")
    public List<Event> getAll() throws SQLException {
        return this.repository.findAll();
    }

    @PutMapping("/add")
    public void addEvent(@RequestBody Event e) throws SQLException{
        this.repository.save(e);
    }

    @DeleteMapping("{id}")
    public void delEventID(@PathVariable int id) throws SQLException{
            this.repository.deleteOneByID(id);;
    }

    @DeleteMapping("/delByLabel")
    public void delEventLabel(@PathVariable String label) throws SQLException{
        if (this.repository.checkLabel(label)) {
            this.repository.deleteOneByLabel(label);
        } else {
            System.out.println("Le label saisi n'existe pas dans la BDD !");
        }
    }
    @PutMapping("{id}")
    public void updateEvent(@PathVariable int id, @RequestBody Event e) throws SQLException{
            this.repository.updateOne(e, id);
    }

    @PostMapping
    public Event create() throws Exception{
        throw new NoteYetImplementedException();
    }


    // Injection de la réponse générer par Spring
    @PostMapping
    public Event create(HttpServletResponse response){
        response.setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        // https://http.cat
        return null;
    }
    // Par le retour de la fonction
    @PostMapping
    public ResponseEntity<Event> create1(){
        return ResponseEntity.ok(new Event());
     // return ResponseEntity.of(null).status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    // Par exception ré-écrite
    @PostMapping
    public Event create2(){
        throw new RuntimeException("Bogué !");
    }

    @ExceptionHandler(Exception.class)
    public Map<String, String> errors(Exception e) {
        return Collections.singletonMap("error", "ça a boogué !");
    }

    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> errors(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return Collections.singletonMap("error", e.getMessage());
    }

    // Par custom exception
    @PostMapping
    public Event create() throws Exception{
        throw new Exception("Bogué !");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class MyException extends Exception {
        private static final long serialVersionUID = 1L;

        public MyException(final String msg) {
            super(msg);
        }
*/
