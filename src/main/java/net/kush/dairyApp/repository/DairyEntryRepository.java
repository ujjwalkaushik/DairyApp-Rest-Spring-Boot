package net.kush.dairyApp.repository;

import net.kush.dairyApp.entity.DairyEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DairyEntryRepository extends MongoRepository<DairyEntry, ObjectId> {

}
