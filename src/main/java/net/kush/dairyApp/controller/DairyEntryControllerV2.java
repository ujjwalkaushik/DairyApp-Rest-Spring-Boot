package net.kush.dairyApp.controller;

import net.kush.dairyApp.entity.DairyEntry;
import net.kush.dairyApp.service.DairyEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/dairy")
public class DairyEntryControllerV2 {

    @Autowired
    private DairyEntryService dairyEntryService;

    @GetMapping
    public List<DairyEntry> getAll() {
        return dairyEntryService.getAll();
    }

    @PostMapping
    public DairyEntry createEntry(@RequestBody DairyEntry entry) {
        entry.setDate(LocalDateTime.now());
        dairyEntryService.saveEntry(entry);
        return entry;
    }

    @GetMapping("id/{entryId}")
    public DairyEntry getDairyEntryById(@PathVariable ObjectId entryId) {
        return dairyEntryService.findDairyById(entryId).orElse(null);
    }

    @DeleteMapping("id/{entryId}")
    public boolean deleteDairyEntryById(@PathVariable ObjectId entryId) {
        dairyEntryService.deleteById(entryId);
        return true;
    }

    @PutMapping("id/{entryId}")
    public  DairyEntry updateDairyById(@PathVariable ObjectId entryId, @RequestBody DairyEntry entry) {
        DairyEntry oldDairyEntry = dairyEntryService.findDairyById(entryId).orElse(null);
        if(oldDairyEntry != null) {
            oldDairyEntry.setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle() : oldDairyEntry.getTitle());
            oldDairyEntry.setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent() : oldDairyEntry.getContent());
        }
        dairyEntryService.saveEntry(oldDairyEntry);
        return oldDairyEntry;
    }
}
