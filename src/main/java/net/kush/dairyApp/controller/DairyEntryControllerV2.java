package net.kush.dairyApp.controller;

import net.kush.dairyApp.entity.DairyEntry;
import net.kush.dairyApp.entity.User;
import net.kush.dairyApp.service.DairyEntryService;
import net.kush.dairyApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dairy")
public class DairyEntryControllerV2 {

    @Autowired
    private DairyEntryService dairyEntryService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getAllDairyByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User byUserName = userService.findByUserName(userName);
        List<DairyEntry> allEntry = byUserName.getDairyEntries();
        if(allEntry != null && !allEntry.isEmpty()) {
            return new ResponseEntity<>(allEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<DairyEntry> createEntry(@RequestBody DairyEntry entry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            dairyEntryService.saveEntry(entry, userName);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{entryId}")
    public ResponseEntity<DairyEntry> getDairyEntryById(@PathVariable ObjectId entryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<DairyEntry> collect = user.getDairyEntries().stream().filter(x -> x.getId().equals(entryId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<DairyEntry> dairyEntry = dairyEntryService.findDairyById(entryId);
            if(dairyEntry.isPresent()) {
                return new ResponseEntity<>(dairyEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{entryId}")
    public ResponseEntity<?> deleteDairyEntryById(@PathVariable ObjectId entryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = dairyEntryService.deleteById(entryId, userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{entryId}")
    public  ResponseEntity<?> updateDairyById(
            @PathVariable ObjectId entryId,
            @RequestBody DairyEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<DairyEntry> collect = user.getDairyEntries().stream().filter(x -> x.getId().equals(entryId)).toList();
        if(!collect.isEmpty()) {
            Optional<DairyEntry> dairyEntry = dairyEntryService.findDairyById(entryId);
            if(dairyEntry.isPresent()) {
                DairyEntry oldDairyEntry = dairyEntry.get();
                oldDairyEntry.setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle() : oldDairyEntry.getTitle());
                oldDairyEntry.setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent() : oldDairyEntry.getContent());
                dairyEntryService.saveEntry(oldDairyEntry, userName);
                return new ResponseEntity<>(oldDairyEntry, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
