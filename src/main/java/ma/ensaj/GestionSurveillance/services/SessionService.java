package ma.ensaj.GestionSurveillance.services;


import jakarta.transaction.Transactional;
import ma.ensaj.GestionSurveillance.entities.Session;
import ma.ensaj.GestionSurveillance.repositories.SessionRepository;
import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId);
    }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public void deleteSession(Long sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public Long countSessions() {
        return sessionRepository.count();
    }

    public List<Session> importSessionsFromCsv(MultipartFile file) {
        List<Session> sessions = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            reader.readNext(); // Skip the header line
            while ((nextLine = reader.readNext()) != null) {
                Session session = new Session();
                session.setType(nextLine[0]);
                session.setStartDate(LocalDate.parse(nextLine[1]));
                session.setEndDate(LocalDate.parse(nextLine[2]));
                session.setDebutMatin1(nextLine[3]);
                session.setFinMatin1(nextLine[4]);
                session.setDebutMatin2(nextLine[5]);
                session.setFinMatin2(nextLine[6]);
                session.setDebutSoir1(nextLine[7]);
                session.setFinSoir1(nextLine[8]);
                session.setDebutSoir2(nextLine[9]);
                session.setFinSoir2(nextLine[10]);

                sessions.add(session);
            }
            sessionRepository.saveAll(sessions);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'importation du fichier CSV: " + e.getMessage());
        }
        return sessions;
    }


}

