package net.kush.dairyApp.service;


import net.kush.dairyApp.entity.DairyEntry;
import net.kush.dairyApp.entity.User;
import net.kush.dairyApp.repository.DairyEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DairyEntryService {

    @Autowired
    private DairyEntryRepository dairyEntryRepository;

    @Autowired
    UserService userService;

    @Transactional
    public void  saveEntry(DairyEntry dairyEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            dairyEntry.setDate(LocalDateTime.now());
            DairyEntry savedEntry = dairyEntryRepository.save(dairyEntry);
            user.getDairyEntries().add(savedEntry);
            userService.saveUser(user);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public void  saveEntry(DairyEntry dairyEntry) {
        dairyEntryRepository.save(dairyEntry);
    }

    public List<DairyEntry> getAll() {
        return dairyEntryRepository.findAll();
    }

    public Optional<DairyEntry> findDairyById(ObjectId id) {
        return dairyEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getDairyEntries().removeIf(entry -> entry.getId().equals(id));
            if(removed) {
                userService.saveUser(user);
                dairyEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting the entry",e);
        }
        return removed;
    }
}
