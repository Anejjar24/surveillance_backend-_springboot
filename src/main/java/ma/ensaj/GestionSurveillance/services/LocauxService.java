package ma.ensaj.GestionSurveillance.services;

import com.opencsv.CSVReader;
import ma.ensaj.GestionSurveillance.entities.Locaux;
import ma.ensaj.GestionSurveillance.repositories.LocauxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocauxService {

    @Autowired
    private LocauxRepository locauxRepository;

    public List<Locaux> getAllLocaux() {
        return locauxRepository.findAll();
    }

    public Optional<Locaux> getLocalById(Long id) {
        return locauxRepository.findById(id);
    }

    public Locaux saveLocal(Locaux locaux) {
        return locauxRepository.save(locaux);
    }

    public void deleteLocal(Long id) {
        locauxRepository.deleteById(id);
    }

    public long countLocaux() {
        return locauxRepository.count();
    }
    public List<Locaux> importLocauxFromCsv(MultipartFile file) {
        List<Locaux> locaux = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            reader.readNext(); // Skip header line
            while ((nextLine = reader.readNext()) != null) {
                Locaux local = new Locaux();
                local.setNom(nextLine[0]);
                local.setTaille(Integer.parseInt(nextLine[1]));
                local.setType(nextLine[2]);
                locaux.add(local);
            }
            return locauxRepository.saveAll(locaux);
        } catch (Exception e) {
            throw new RuntimeException("Error importing locaux from CSV: " + e.getMessage());
        }
    }
}

