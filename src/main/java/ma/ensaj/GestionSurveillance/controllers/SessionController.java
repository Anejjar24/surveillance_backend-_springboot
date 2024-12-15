package ma.ensaj.GestionSurveillance.controllers;

import ma.ensaj.GestionSurveillance.entities.Session;
import ma.ensaj.GestionSurveillance.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping("/")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        Optional<Session> session = sessionService.getSessionById(id);
        return session.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public Session createSession(@RequestBody Session session) {
        return sessionService.saveSession(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session) {
        Optional<Session> existingSession = sessionService.getSessionById(id);
        if (existingSession.isPresent()) {
            session.setSession_id(id);
            return ResponseEntity.ok(sessionService.saveSession(session));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        Optional<Session> existingSession = sessionService.getSessionById(id);
        if (existingSession.isPresent()) {
            sessionService.deleteSession(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/count", produces = { "application/json", "application/xml" })
    public Long countSessions() {
        return sessionService.countSessions();
    }
}