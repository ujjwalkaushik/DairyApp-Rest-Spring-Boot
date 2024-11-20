package net.kush.dairyApp.controller;

import net.kush.dairyApp.entity.DairyEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dairy")
public class DairyEntryController {


    private Map<Long, DairyEntry> dairyEntries= new HashMap<>();

    @GetMapping()
    public List<DairyEntry> getAll() {
        return new ArrayList<>(dairyEntries.values());
    }


    @PostMapping
    public boolean createEntry(@RequestBody DairyEntry entry) {
        dairyEntries.put(entry.getId(), entry);
        return true;
    }

    @GetMapping("id/{entryId}")
    public DairyEntry getDairyEntryById(@PathVariable("entryId") Long entryId) {
        return dairyEntries.get(entryId);
    }

    @DeleteMapping("id/{entryId}")
    public DairyEntry deleteDairyEntryById(@PathVariable("entryId") Long entryId) {
        return dairyEntries.remove(entryId);
    }

    @PutMapping("id/{entryId}")
    public  DairyEntry updateDairyById(@PathVariable("entryId") Long entryId, @RequestBody DairyEntry entry) {
            return dairyEntries.put(entryId, entry);
    }
}
