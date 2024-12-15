package ma.ensaj.GestionSurveillance.services;

import ma.ensaj.GestionSurveillance.entities.Locaux;
import ma.ensaj.GestionSurveillance.repositories.LocauxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

