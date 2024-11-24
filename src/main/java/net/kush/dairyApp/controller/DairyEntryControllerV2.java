package net.kush.dairyApp.controller;

import net.kush.dairyApp.entity.DairyEntry;
import net.kush.dairyApp.service.DairyEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dairy")
public class DairyEntryControllerV2 {

    @Autowired
    private DairyEntryService dairyEntryService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<DairyEntry> allEntry = dairyEntryService.getAll();
        if(allEntry != null && !allEntry.isEmpty()) {
            return new ResponseEntity<>(allEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<DairyEntry> createEntry(@RequestBody DairyEntry entry) {
        try {
            entry.setDate(LocalDateTime.now());
            dairyEntryService.saveEntry(entry);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{entryId}")
    public ResponseEntity<DairyEntry> getDairyEntryById(@PathVariable ObjectId entryId) {
        Optional<DairyEntry> dairyEntry = dairyEntryService.findDairyById(entryId);
        return dairyEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("id/{entryId}")
    public ResponseEntity<?> deleteDairyEntryById(@PathVariable ObjectId entryId) {
        dairyEntryService.deleteById(entryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{entryId}")
    public  ResponseEntity<?> updateDairyById(@PathVariable ObjectId entryId, @RequestBody DairyEntry entry) {
        DairyEntry oldDairyEntry = dairyEntryService.findDairyById(entryId).orElse(null);
        if(oldDairyEntry != null) {
            oldDairyEntry.setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle() : oldDairyEntry.getTitle());
            oldDairyEntry.setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent() : oldDairyEntry.getContent());
            dairyEntryService.saveEntry(oldDairyEntry);
            return new ResponseEntity<>(oldDairyEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
