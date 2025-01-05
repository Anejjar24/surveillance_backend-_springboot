package ma.ensaj.GestionSurveillance.controllers;

import ma.ensaj.GestionSurveillance.entities.Session;
import ma.ensaj.GestionSurveillance.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
    @PostMapping("/import")
    public ResponseEntity<List<Session>> importSessions(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<Session> sessions = sessionService.importSessionsFromCsv(file);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{id}/table")
    public ResponseEntity<Map<String, Object>> getSessionSchedule(@PathVariable Long id) {
        Optional<Session> optionalSession = sessionService.getSessionById(id);
        if (optionalSession.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Session session = optionalSession.get();
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("startDate", session.getStartDate());
        schedule.put("endDate", session.getEndDate());

        List<Map<String, String>> timeSlots = new ArrayList<>();

        // Créneau du matin 1
        Map<String, String> matin1 = new HashMap<>();
        matin1.put("time", session.getDebutMatin1() + " - " + session.getFinMatin1());
        matin1.put("slot", "morning1");
        timeSlots.add(matin1);

        // Créneau du matin 2
        Map<String, String> matin2 = new HashMap<>();
        matin2.put("time", session.getDebutMatin2() + " - " + session.getFinMatin2());
        matin2.put("slot", "morning2");
        timeSlots.add(matin2);

        // Créneau du soir 1
        Map<String, String> soir1 = new HashMap<>();
        soir1.put("time", session.getDebutSoir1() + " - " + session.getFinSoir1());
        soir1.put("slot", "evening1");
        timeSlots.add(soir1);

        // Créneau du soir 2
        Map<String, String> soir2 = new HashMap<>();
        soir2.put("time", session.getDebutSoir2() + " - " + session.getFinSoir2());
        soir2.put("slot", "evening2");
        timeSlots.add(soir2);

        schedule.put("timeSlots", timeSlots);
        return ResponseEntity.ok(schedule);
    }
}