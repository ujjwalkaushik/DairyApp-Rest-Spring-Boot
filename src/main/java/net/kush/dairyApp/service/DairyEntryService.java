package net.kush.dairyApp.service;


import net.kush.dairyApp.entity.DairyEntry;
import net.kush.dairyApp.repository.DairyEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DairyEntryService {

    @Autowired
    private DairyEntryRepository dairyEntryRepository;

    public void  saveEntry(DairyEntry dairyEntry) {
        dairyEntryRepository.save(dairyEntry);
    }

    public List<DairyEntry> getAll() {
        return dairyEntryRepository.findAll();
    }

    public Optional<DairyEntry> findDairyById(ObjectId id) {
        return dairyEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        dairyEntryRepository.deleteById(id);
    }
}
